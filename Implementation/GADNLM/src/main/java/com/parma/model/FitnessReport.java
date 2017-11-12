package com.parma.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "FitnessReport")
public class FitnessReport {

  private double averageScore;
  private double bestScore;
  private int generation;
  private String calibration;
  private String owner;
  
  public FitnessReport(double pAverageScore, double pBestScore, int pGeneration, String pCalibration, String pOwner) {
	  averageScore = pAverageScore;
	  bestScore = pBestScore;
	  generation = pGeneration;
	  calibration = pCalibration;
	  owner = pOwner;
  }
  public FitnessReport() {
    
  }
  
  public double getAverageScore() {
    return averageScore;
  }
  
  public void setAverageScore(double averageScore) {
    this.averageScore = averageScore;
  }
  
  public double getBestScore() {
    return bestScore;
  }
  
  public void setBestScore(double bestScore) {
    this.bestScore = bestScore;
  }
  
  public int getGeneration() {
    return generation;
  }
  
  public void setGeneration(int generation) {
    this.generation = generation;
  }
  
  public String getCalibrationName() {
    return calibration;
  }
  
  public void setCalibrationName(String calibrationName) {
    this.calibration = calibrationName;
  }
  
  public String getOwner() {
    return owner;
  }
  
  public void setOwner(String owner) {
    this.owner = owner;
  }  
  
  
}
