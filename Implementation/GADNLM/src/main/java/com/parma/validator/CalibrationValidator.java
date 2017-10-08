package com.parma.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.parma.model.Calibration;

@Component
public class CalibrationValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return Calibration.class.equals(aClass);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Calibration cal = (Calibration) target;
    
    if (cal.getLower_w() < 1 || cal.getLower_w() > cal.getUpper_w()) {
      errors.rejectValue("w", "Incorrect w values");
    }
    
    if (cal.getLower_w_n() < 1 || cal.getLower_w_n() > cal.getUpper_w_n()) {
      errors.rejectValue("w_n", "Incorrect w_n values");
    }
    
    if (cal.getLower_s_r() < 1 || cal.getLower_s_r() > cal.getUpper_s_r()) {
      errors.rejectValue("sigma_r", "Incorrect sigma r values");
    }       
    
    if (cal.getMax_ind() < 1) {
      errors.rejectValue("max_ind", "Invalid number of maximum individuals");
    }
    
    if (cal.getMax_gen() < 1) {
      errors.rejectValue("max_gen", "Invalid number of maximum generations");
    }
    
    if (cal.getMut_perc() < 0 || cal.getMut_perc() > 1) {
      errors.rejectValue("mut_perc", "Invalid mutation percentage");
    }
    
    if (cal.getOriginalImages() == null) {
      errors.rejectValue("originalImages", "Original images are missing.");
    }
    
    if (cal.getGroundtruthImages() == null) {
      errors.rejectValue("groundtruthImages", "Groundtruth images are missing.");
    }
        
    if (!cal.getGroundtruthFilenames().equals(cal.getOriginalFilenames())) {
      errors.rejectValue("originalImages", "Source and groundtruth mismatch.");
    }
    
  }

  
}
