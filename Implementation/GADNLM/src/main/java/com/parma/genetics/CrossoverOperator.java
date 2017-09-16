package com.parma.genetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.parma.genetics.settings.Crossover;

public class CrossoverOperator {
	private Crossover type;
	
	public CrossoverOperator(Crossover type) {
		this.type = type;
	}
	
	public List<ParamIndividual> cross(List<ParamIndividual> parents) {
		List<ParamIndividual> offspring = new ArrayList<ParamIndividual>();
		if (type == Crossover.SIMPLE) {
			for(int ind = 0; ind < parents.size();ind++) {
				Collections.shuffle(parents);
				ParamIndividual p = new ParamIndividual();
				ParamIndividual parent1 = parents.get(0);
				ParamIndividual parent2 = parents.get(1);
				
				
			
				
			}
		}
		
		return offspring;
		
	}
	
}
