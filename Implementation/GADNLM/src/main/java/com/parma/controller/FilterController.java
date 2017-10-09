package com.parma.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.parma.dal.CalibrationDal;
import com.parma.model.Calibration;
import com.parma.model.Image;
import com.parma.service.AsyncProcessService;
import com.parma.utils.ControllerUtils;
import com.parma.validator.ImageValidator;

@Controller
public class FilterController {

  @Resource
  AsyncProcessService asyncService;

  @Autowired
  private ImageValidator imgValidator;

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
      @ModelAttribute("image") Image imageForm, BindingResult bindingResult,
      @RequestParam("image_files") MultipartFile[] images) {
    
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    // load the combobox with prev. calibrations
    List<Calibration> calibrations = CalibrationDal.loadFinishedCalibrations();
    model.addAttribute("calibrations", calibrations);

    Image settings = new Image();

    try {
      
      settings.setGroup(request.getParameter("image_group"));
      settings.setOwner(auth.getName());
      settings.setW(Integer.parseInt(request.getParameter("w_value")));
      settings.setW_n(Integer.parseInt(request.getParameter("w_n_value")));
      settings.setS_r(Integer.parseInt(request.getParameter("sigma_r_value")));
      settings.setMatList(ControllerUtils.getNameMatrixMap(images, false));

      // verify integrity of data
      imgValidator.validate(settings, bindingResult);

      if (bindingResult.hasErrors()) {

        model.addAttribute("message", "The filtering could not start");

      } else {

        model.addAttribute("message", "The images are being filtered");

        // run AsyncService to process the images
        asyncService.processImages(settings);
      }

    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    return "filter";
  }


}
