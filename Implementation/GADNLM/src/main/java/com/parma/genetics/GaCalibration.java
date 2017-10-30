package com.parma.genetics;

import java.util.Random;
import java.util.TreeSet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.ArrayList;
import com.parma.dal.CalibrationDal;
import com.parma.dal.ReportDal;
import com.parma.genetics.fitness.FitnessEval;
import com.parma.genetics.settings.Fitness;
import com.parma.genetics.settings.GaSettings;
import com.parma.model.FitnessReport;
import com.parma.model.TimeReport;

public class GaCalibration {

  private Population population;

  private int safeboxSize;

  private TreeSet<ParamIndividual> safebox;

  private GaSettings settings;

  private final Random random = new Random();

  public GaCalibration(GaSettings settings) {
    this.settings = settings;
    population = new Population(settings);
    this.population.initializePopulation(settings.getMaxIndividuals());
    this.safeboxSize = Math.max(1, (int) ((double) settings.getMaxIndividuals() * 0.2));
    this.safebox = new TreeSet<ParamIndividual>();

    this.settings.setSelectionThreshold(0.6);
  }


  public void runCalibration() {

    ParamIndividual bestIndividual = new ParamIndividual();
    CrossoverOperator crossover = new CrossoverOperator(settings.getCrossoverType());
    
    // run GA for the number of generations specified in settings
    for (int gen = 0; gen < settings.getMaxGenerations(); gen++) {

      /* fitness function step */

      calculatePopulationFitness(gen);
      population.sortByFitness();

      bestIndividual = population.getIndividual(0);
      safebox.add(bestIndividual);

      if (safebox.size() > this.safeboxSize) {
        safebox.remove(safebox.last());
      }

      // ParamIndividual worstIndividual = population.getIndividual(population.getSize() - 1);

      /*
       * update the Calibration in the database
       */
      String status = "RUNNING";
      if (gen == settings.getMaxGenerations()-1) {
        status = "DONE";
      }
      CalibrationDal.updateStatus(settings.getTitle(), bestIndividual.getFitness(), gen + 1,
          status, settings.getOwner());

      /*
       * Generate and save the fitness report for each generation
       */
      FitnessReport fitnessReport = new FitnessReport(getAverageFitness(),
          bestIndividual.getFitness(), gen + 1, settings.getTitle(), settings.getOwner());


      ReportDal.saveFitnessReport(fitnessReport);

      /* selection step */
      normalizePopulationFitness();

      List<ParamIndividual> selectionIndividuals = getSelectionIndividuals();
      System.out.println("Number of parents: " + selectionIndividuals.size());
      List<ParamIndividual> offspring =
          crossover.cross(selectionIndividuals, (int) (settings.getMaxIndividuals() / 2));

      population.update(offspring);

      applyMutation();
    }
    
    /*
     * Update calibration with final status and parameters
     */
    
    /*
     * CalibrationDal.updateStatus(settings.getTitle(), bestIndividual.getFitness(),
     * settings.getMaxGenerations(), "DONE", settings.getOwner());
     */
        

    /*
    FitnessReport fitnessReport =
        new FitnessReport(getAverageFitness(), bestIndividual.getFitness(),
            settings.getMaxGenerations(), settings.getTitle(), settings.getOwner());
    ReportDal.saveFitnessReport(fitnessReport);
     */

    bestIndividual = safebox.first();

    CalibrationDal.updateParams(settings.getTitle(), bestIndividual.getW(), bestIndividual.getW_n(),
        bestIndividual.getSigma_r(), bestIndividual.getFitness(), settings.getOwner());

  }


  public Population getPopulation() {
    return population;
  }


  public void setPopulation(Population population) {
    this.population = population;
  }


  private double getAverageFitness() {
    double averageFitness = 0;
    for (int ind = 0; ind < settings.getMaxIndividuals(); ind++) {
      averageFitness += population.getIndividual(ind).getFitness();
    }
    averageFitness = averageFitness / settings.getMaxIndividuals();
    return averageFitness;
  }


  private void calculatePopulationFitness(int gen) {
    long start;
    long genStart = System.currentTimeMillis();

    for (int ind = 0; ind < settings.getMaxIndividuals(); ind++) {

      ParamIndividual p = population.getIndividual(ind);
      FitnessEval fitEval =
          new FitnessEval(settings.getFitnessFunction(), settings.getSegmentationTechnique());

      double score = 0;
      if (settings.getFitnessFunction() == Fitness.DICE) {


        start = System.currentTimeMillis();
        for (int index = 0; index < settings.getSampleCount(); index++) {
          score += fitEval.evaluate(p, settings.getOriginalImage(index),
              settings.getGroundtruthImage(index));
        }

        // Save the time report for an individual
        TimeReport timeReport = new TimeReport(System.currentTimeMillis() - start, "INDIVIDUAL",
            gen + 1, settings.getTitle(), settings.getOwner());
        ReportDal.saveTimeReport(timeReport);

      }

      // calculate the mean score
      score = score / (double) settings.getSampleCount();
      population.getIndividual(ind).setFitness(score);

    }
    // Save the time report for a generation
    TimeReport timeReport = new TimeReport(System.currentTimeMillis() - genStart, "GENERATION",
        gen + 1, settings.getTitle(), settings.getOwner());
    ReportDal.saveTimeReport(timeReport);

  }


  private void normalizePopulationFitness() {
    double accumulatedFitness = getAccumulatedFitness();
    for (int ind = 0; ind < settings.getMaxIndividuals(); ind++) {
      ParamIndividual p = population.getIndividual(ind);
      double normFitness = p.getFitness() / accumulatedFitness;
      p.setFitness(normFitness);
    }
  }


  private List<ParamIndividual> getSelectionIndividuals() {

    double individualAccumulatedFitness = 1;
    List<ParamIndividual> selectedIndividuals = new ArrayList<ParamIndividual>();
    double threshold = settings.getSelectionThreshold();

    int index = 0;
    while (individualAccumulatedFitness >= threshold && index < population.getSize()) {
      ParamIndividual p = population.getIndividual(index);
      selectedIndividuals.add(p);
      individualAccumulatedFitness -= p.getFitness();
      index++;
    }

    return selectedIndividuals;
  }


  private void applyMutation() {
    Mutator mutator = new Mutator(settings.getMutationType());
    double mutationFactor = random.nextDouble();
    for (int index = 0; index < settings.getMaxIndividuals(); index++) {
      if (mutationFactor <= settings.getMutationPerc()) {
        ParamIndividual p = population.getIndividual(index);
        mutator.mutate(p);
      }
      mutationFactor = random.nextDouble();
    }
  }


  private double getAccumulatedFitness() {
    double accumulatedFitness = 0;
    for (int ind = 0; ind < settings.getMaxIndividuals(); ind++) {
      ParamIndividual p = population.getIndividual(ind);
      accumulatedFitness += p.getFitness();
    }
    return accumulatedFitness;
  }

}
