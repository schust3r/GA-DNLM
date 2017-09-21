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
    population = new Population(settings);
    this.population.initializePopulation(settings.getMaxIndividuals());
    this.safeboxSize = (int) ((double) settings.getMaxIndividuals() * 0.2);
  }

  
  public void runCalibration() {

    // run GA for the number of generations specified in settings
    for (int gen = 0; gen < settings.getMaxGenerations(); gen++) {
      CrossoverOperator crossover = new CrossoverOperator(settings.getCrossoverType());
      /* fitness function step */
      
      
      
      calculatePopulationFitness();
      population.sortByFitness();
      
      
      
      ParamIndividual bestIndividual = population.getIndividual(0);
      ParamIndividual worstIndividual = population.getIndividual(population.getSize()-1);
      
      System.out.println("Iteracion "+gen+" | average fitness: "+ getAverageFitness());
      
      System.out.println(bestIndividual.getFitness());
      System.out.println(bestIndividual.getW()+bestIndividual.getW_n()+bestIndividual.getSigma_r());

      System.out.println(worstIndividual.getFitness());
      System.out.println(worstIndividual.getW()+worstIndividual.getW_n()+worstIndividual.getSigma_r());
      
      System.out.println("----------------");
      
      /* selection step */
      normalizePopulationFitness();

      List<ParamIndividual> selectionIndividuals = getSelectionIndividuals();
      List<ParamIndividual> offspring = crossover.cross(selectionIndividuals,(int)( population.getSize()/2));
      
      population.update(offspring);
      
      
      
      
      /*
       * Put code here
       * 
       */
      
      applyMutation();
 
      
    }
  }

  private double getAverageFitness() {
	  double averageFitness = 0;
	  for(int ind = 0 ; ind < settings.getMaxIndividuals();ind++) {
		  averageFitness += population.getIndividual(ind).getFitness();
		  
	  }
	  averageFitness = averageFitness / settings.getMaxIndividuals();
	  return averageFitness;
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
      population.getIndividual(ind).setFitness(score);
    }
  }
  
  

  private void normalizePopulationFitness() {
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
		  selectedIndividuals.add(p);
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
