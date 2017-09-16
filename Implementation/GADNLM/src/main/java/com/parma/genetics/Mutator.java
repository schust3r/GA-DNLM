package com.parma.genetics;

import java.util.Random;

import com.parma.genetics.settings.Mutation;

public class Mutator {
	
	private Mutation type;
	private final Random random = new Random();
	
	public Mutator(Mutation mutationType) {
		setType(mutationType);
		// TODO Auto-generated constructor stub
	}

	public Mutation getType() {
		return type;
	}

	public void setType(Mutation type) {
		this.type = type;
	}

	
	public void mutate(ParamIndividual p) {
		
		if (type == Mutation.RANDOM_BIT) {
			int bitPlace = random.nextInt(6);
			int param = random.nextInt(3);
			if (param == 0) {
				//
			}
			if (param == 1) {
				int newW_n = p.getW_n() ^ (1 << bitPlace);
				p.setW_n(newW_n);
			}
			else {
				int newSigma_r = p.getSigma_r() ^ (1 << bitPlace);
				p.setSigma_r(newSigma_r);
			}
		}
	}
	
}
