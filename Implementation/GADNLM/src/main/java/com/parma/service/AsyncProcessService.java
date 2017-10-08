package com.parma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.parma.genetics.GaCalibration;
import com.parma.genetics.settings.GaSettings;

@Service
public class AsyncProcessService {

  Logger log = LoggerFactory.getLogger(this.getClass().getName());
  
  @Async
  public void processCalibration(GaSettings settings) {
    
    log.info("Calibration " + settings.getTitle()+ " has begun");
    
    // create and run a GA calibration instance
    GaCalibration gaExecution = new GaCalibration(settings);
    gaExecution.runCalibration();    
    
  }
  
  
  
}
