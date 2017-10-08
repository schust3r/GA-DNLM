package com.parma.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.parma.dal.CalibrationDal;
import com.parma.model.Calibration;
import com.parma.model.Image;
import com.parma.service.AsyncProcessService;

@Controller
public class FilterController {
  
  @Resource
  AsyncProcessService asyncService;

  @RequestMapping(value = "/filter", method = RequestMethod.GET)
  public String dashboard(HttpServletRequest servletRequest, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    
    List<Calibration> calibrations = CalibrationDal.loadFinishedCalibrations();
    model.addAttribute("calibrations", calibrations);
    
    return "filter";
  }
  
  @RequestMapping(value = "/run-filter", method = RequestMethod.POST)
  public String runCalibration(HttpServletRequest request, Model model,      
      @RequestParam("image_group") String group, BindingResult bindingResult,
      @RequestParam("image_files") MultipartFile[] images) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    // create instance of image to pass parameters
    Image image = new Image();
    image.setGroup(group);
    image.setFilelist(images);

    return "calibrate";
  }
  
  
}
