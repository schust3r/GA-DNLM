package com.parma.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TimeReport")
public class TimeReport {

  private long timeElapsed;
  private String type;
  private int generation;
  private String calibration;
  private String owner;
  
  public TimeReport(long timeElapsed, String type, int generation, String calibrationName, String owner) {
    this.timeElapsed = timeElapsed;
    this.type = type;
    this.generation = generation;
    this.calibration = calibrationName;
    this.owner = owner;
}
  
  public long getTimeElapsed() {
    return timeElapsed;
  }
  
  public void setTimeElapsed(long timeElapsed) {
    this.timeElapsed = timeElapsed;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
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
