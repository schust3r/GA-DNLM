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
    
    if (cal.getLower_w() > cal.getUpper_w()) {
      errors.rejectValue("w", "Incorrect w values");
    }
    
    if (cal.getLower_w_n() > cal.getUpper_w_n()) {
      errors.rejectValue("wn", "Incorrect w_n values");
    }
    
    if (cal.getLower_s_r() > cal.getUpper_s_r()) {
      errors.rejectValue("sigmar", "Incorrect sigma r values");
    }       
    
    if (cal.getOriginalImages() == null && cal.getGroundtruthImages() != null) {
      errors.rejectValue("images", "Not all required images were found.");
    }
    
  }

  
}
