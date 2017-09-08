package com.parma.genetics;

/**
 * 
 * @author Joel Schuster
 *
 */

public class ParamIndividual implements Comparable<ParamIndividual> {
	
	private float lambda;
	private int sigma_r;
	private int sigma_s;
	private float fitness;
	
	public ParamIndividual() {
		this.setFitness(0);
	}
	
	public float getLambda() {
		return lambda;
	}
	
	public void setLambda(float lambda) {
		this.lambda = lambda;
	}
	
	public int getSigma_r() {
		return sigma_r;
	}
	
	public void setSigma_r(int sigma_r) {
		this.sigma_r = sigma_r;
	}
	
	public int getSigma_s() {
		return sigma_s;
	}
	
	public void setSigma_s(int sigma_s) {
		this.sigma_s = sigma_s;
	}

	public float getFitness() {
		return fitness;
	}

	public void setFitness(float fitness) {
		this.fitness = fitness;
	}
	
	/**
	 * Implement Comparator.
	 * Individuals with a higher fitness will be ordered higher.
	 */

	@Override
	public int compareTo(ParamIndividual other) { 
		return Double.compare(this.fitness, other.fitness);
	}

}
