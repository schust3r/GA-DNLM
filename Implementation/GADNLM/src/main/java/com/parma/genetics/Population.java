package com.parma.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.parma.genetics.settings.GaSettings;

public class Population {

	private List<ParamIndividual> population;
	
	// safeguard the best individuals encountered
	private List<ParamIndividual> safebox;
	
	private final Random random = new Random();
	
	private final GaSettings settings;
	
	public Population(GaSettings settings) {
		this.population = new ArrayList<ParamIndividual>();
		this.safebox = new ArrayList<ParamIndividual>();
		this.settings = settings;
	}
	
	public void initializePopulation(int maxInd) {
		for (int i = 0; i < maxInd; i++) {
			ParamIndividual p = new ParamIndividual();
			
			p.setLambda(getRandomFloatBetween(settings.getLowerLambda(), 
					settings.getUpperLambda()));
			
			p.setSigma_r(getRandomIntegerBetween(settings.getLowerSigmaR(), 
					settings.getUpperSigmaR()));
			
			p.setSigma_s(getRandomIntegerBetween(settings.getLowerSigmaS(), 
					settings.getUpperSigmaS()));
			
			population.add(p);
		}
	}
	
	public void addBestToSafebox() {
		safebox.add(population.get(0));
	}
	
	public void removeWorstFromSafebox() {
		safebox.remove(safebox.size() - 1);
	}
	
	public int getSize() {
		return this.population.size();
	}
	
	private int getRandomIntegerBetween(int lower, int upper) {
		return random.nextInt(upper - lower + 1) + lower;
	}
	
	private float getRandomFloatBetween(float lower, float upper) {
		return random.nextFloat() * (upper - lower) + lower;
	}
	
}
