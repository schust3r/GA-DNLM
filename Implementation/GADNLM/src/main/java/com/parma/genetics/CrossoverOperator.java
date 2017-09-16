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
		
		if (type == Crossover.CLUSTER || type == Crossover.SIMPLE) {
			double fitnessAccum = 0;
			for(int ind = 0; ind < parents.size();ind++) {
				
				Collections.shuffle(parents);
				ParamIndividual p = new ParamIndividual();

				int lim = 2; 
				if (type == Crossover.CLUSTER) lim += 4; //No hay referencia al respecto
				
				for(int i = 0; i < lim; i++) {
					fitnessAccum += parents.get(i).getFitness();
				}
				
				double w = 0;
				double w_n = 0;
				double sigma_r = 0;
				
				for(int i=0; i < lim; i++) {
					w += parents.get(i).getW()*(parents.get(i).getFitness()/fitnessAccum);
					w_n += parents.get(i).getW_n()*(parents.get(i).getFitness()/fitnessAccum);
					sigma_r += parents.get(i).getSigma_r()*(parents.get(i).getFitness()/fitnessAccum);
				}
				
				p.setW((int) w);
				p.setW_n((int) w_n);
				p.setSigma_r((int) sigma_r);
				offspring.add(p);
				
			}	
		}
		
		else {
			for(int ind = 0; ind < parents.size();ind++) {
				
				Collections.shuffle(parents);
				ParamIndividual p = new ParamIndividual();
				
				int w = parents.get(0).getW();
				int w_n = parents.get(0).getW_n();
				int sigma_r = parents.get(0).getSigma_r();
				
				w = w | parents.get(1).getW();

				w_n= w_n | parents.get(1).getW_n();

				sigma_r = sigma_r | parents.get(1).getSigma_r();
				
				
				p.setW((int) w);
				p.setW_n((int) w_n);
				p.setSigma_r((int) sigma_r);
				
				offspring.add(p);
				
			}
					
			
		}
		
		return offspring;
		
	}
	
}
