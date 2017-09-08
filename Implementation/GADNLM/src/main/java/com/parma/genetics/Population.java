package com.parma.genetics;

import java.util.List;
import java.util.Random;

public class Population {

	private List<ParamIndividual> population;
	
	// safeguard the best individuals encountered
	private List<ParamIndividual> safebox;
	
	private final Random random = new Random();
	
	public Population() {
		
	}
	
	public void initializePopulation(int maxInd) {
		for (int i = 0; i < maxInd; i++) {
			ParamIndividual p = new ParamIndividual();
			
		}
	}
	
}
