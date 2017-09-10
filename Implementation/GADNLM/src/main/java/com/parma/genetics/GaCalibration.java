package com.parma.genetics;

import java.util.Random;
import com.parma.genetics.fitness.FitnessEval;
import com.parma.genetics.settings.Fitness;
import com.parma.genetics.settings.GaSettings;

public class GaCalibration {

  private Population population;

  private int safeboxSize;

  private GaSettings settings;

  private final Random random = new Random();

  public GaCalibration(GaSettings settings) {
    this.settings = settings;
    this.population.initializePopulation(settings.getMaxIndividuals());
    this.safeboxSize = (int) ((double) settings.getMaxIndividuals() * 0.2);
  }

  public void runCalibration() {

    // run GA for the number of generations specified in settings
    for (int gen = 0; gen < settings.getMaxGenerations(); gen++) {

      /* fitness function step */
      calculatePopulationFitness();

      /* selection step */
      normalizePopulationFitness();

      double threshold = random.nextDouble();
      
      

    }
  }

  private void calculatePopulationFitness() {
    for (int ind = 0; ind < settings.getMaxIndividuals(); ind++) {
      ParamIndividual p = population.getIndividual(ind);
      FitnessEval fitEval = new FitnessEval(settings.getFitnessFunction());
      double score = 0;
      if (settings.getFitnessFunction() == Fitness.DICE) {
        for (int imgInd = 0; imgInd < settings.getSampleCount(); imgInd++) {
          score += fitEval.evaluate(p, settings.getOriginalImage(imgInd),
              settings.getGroundtruthImage(imgInd));
        }
      }
      p.setFitness(score);
    }
  }

  private void normalizePopulationFitness() {
    population.sortByFitness();
    double maxFitness = population.getIndividual(0).getFitness();
    for (int ind = 0; ind < settings.getMaxIndividuals(); ind++) {
      ParamIndividual p = population.getIndividual(ind);
      double normFitness = p.getFitness() / maxFitness;
      p.setFitness(normFitness);
    }
  }


}
