package com.parma.genetics;

/**
 * 
 * @author Joel Schuster
 *
 */

public class ParamIndividual implements Comparable<ParamIndividual> {

  private int w;
  private int w_n;
  private int sigma_r;
  private double fitness;

  public ParamIndividual() {
    this.setFitness(0);
  }

  public int getW() {
    return w;
  }

  public void setW(int w) {
    this.w = w;
  }

  public int getW_n() {
    return w_n;
  }

  public void setW_n(int w_n) {
    this.w_n = w_n;
  }

  public int getSigma_r() {
    return sigma_r;
  }

  public void setSigma_r(int sigma_r) {
    this.sigma_r = sigma_r;
  }

  public double getFitness() {
    return fitness;
  }

  public void setFitness(double fitness) {
    this.fitness = fitness;
  }

  /**
   * Implement Comparator. Individuals with a higher fitness will be ordered higher.
   */

  @Override
  public int compareTo(ParamIndividual other) {
    return Double.compare(this.fitness, other.fitness);
  }

}
