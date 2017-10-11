package com.parma.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "FitnessReport")
public class FitnessReport {

  private double averageScore;
  private double bestScore;
  private int generation;
  private String calibrationName;
  private String owner;
  
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
    return calibrationName;
  }
  
  public void setCalibrationName(String calibrationName) {
    this.calibrationName = calibrationName;
  }
  
  public String getOwner() {
    return owner;
  }
  
  public void setOwner(String owner) {
    this.owner = owner;
  }  
  
  
}
