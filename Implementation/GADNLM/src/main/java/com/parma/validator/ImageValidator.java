package com.parma.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.parma.model.Image;

@Component
public class ImageValidator implements Validator {

  @Override
  public boolean supports(Class<?> aClass) {
    return Image.class.equals(aClass);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Image cal = (Image) target;
    
    if (cal.getW() < 1) {
      errors.rejectValue("w", "Incorrect w values");
    }
    
    if (cal.getW_n() < 1) {
      errors.rejectValue("w_n", "Incorrect w_n values");
    }
    
    if (cal.getS_r() < 1) {
      errors.rejectValue("s_r", "Incorrect sigma r values");
    }
    
    if (cal.getGroup() == null || cal.getGroup() == "") {
      errors.rejectValue("group", "Invalid group");
    }
    
    if (cal.getOwner() == null || cal.getOwner() == "") {
      errors.rejectValue("owner", "Invalid owner");
    }
    
    if (cal.getMatList() == null) {
      errors.rejectValue("filelist", "Invalid image list");
    }
    
  }
  
  
  
}