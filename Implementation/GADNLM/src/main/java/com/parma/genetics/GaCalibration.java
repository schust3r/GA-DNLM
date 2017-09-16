package com.parma.genetics;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
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
      CrossoverOperator crossover = new CrossoverOperator(settings.getCrossoverType());
      /* fitness function step */
      calculatePopulationFitness();

      /* selection step */
      normalizePopulationFitness();

      List<ParamIndividual> selectionIndividuals = getSelectionIndividuals();
      List<ParamIndividual> offspring = crossover.cross(selectionIndividuals);
      
      

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
    double accumulatedFitness = getAccumulatedFitness();
    for (int ind = 0; ind < settings.getMaxIndividuals(); ind++) {
      ParamIndividual p = population.getIndividual(ind);
      double normFitness = p.getFitness() / accumulatedFitness;
      p.setFitness(normFitness);
    }
  }
  
  private List<ParamIndividual> getSelectionIndividuals(){
	  
	  double individualAccumulatedFitness = 1;
	  List<ParamIndividual> selectedIndividuals = new ArrayList<ParamIndividual>();
	  double threshold = settings.getSelectionThreshold();
	  int ind = 0;
	  while(individualAccumulatedFitness >= threshold) {
		  ParamIndividual p = population.getIndividual(ind);
		  individualAccumulatedFitness -= p.getFitness();
		  ind++;
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
