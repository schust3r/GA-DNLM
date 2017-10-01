package com.parma.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
import com.parma.genetics.GaCalibration;
import com.parma.genetics.settings.GaSettings;
import com.parma.genetics.utils.TypeUtils;
import com.parma.model.Calibration;
import com.parma.model.User;
import com.parma.validator.CalibrationValidator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;

@Controller
public class CalibrateController {

  @Autowired
  private CalibrationValidator calValidator;

  @RequestMapping(value = "/calibrate", method = RequestMethod.GET)
  public String dashboard(@ModelAttribute("user") User userForm, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    return "calibrate";
  }

  @RequestMapping(value = "/run-calibration", method = RequestMethod.POST)
  public String getCalibrationSettings(HttpServletRequest request, Model model,
      @ModelAttribute("calibration") Calibration calForm, BindingResult bindingResult,
      @RequestParam("orig_images") MultipartFile origImages,
      @RequestParam("ground_images") MultipartFile groundImages) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    Calibration cal = new Calibration();

    try {

      // Initial parameters
      cal.setLower_w(Integer.parseInt(request.getParameter("lower_w")));
      cal.setUpper_w(Integer.parseInt(request.getParameter("upper_w")));
      cal.setLower_w_n(Integer.parseInt(request.getParameter("lower_w_n")));
      cal.setUpper_w_n(Integer.parseInt(request.getParameter("upper_w_n")));
      cal.setLower_s_r(Integer.parseInt(request.getParameter("lower_s_r")));
      cal.setUpper_s_r(Integer.parseInt(request.getParameter("upper_s_r")));

      cal.setTitle(request.getParameter("title"));
      cal.setDescription(request.getParameter("description"));
      cal.setMax_ind(Integer.parseInt(request.getParameter("max_ind")));
      cal.setMax_gen(Integer.parseInt(request.getParameter("max_gen")));
      cal.setMut_perc(Double.parseDouble(request.getParameter("mut_perc")));

      cal.setMut_type(request.getParameter("mut_type"));
      cal.setCross_type(request.getParameter("cross_type"));
      cal.setFit_func(request.getParameter("fit_func"));
      cal.setSeg_method(request.getParameter("seg_method"));

      cal.setOriginalImages(origImages);
      cal.setGroundtruthImages(groundImages);

      // verify integrity of data
      calValidator.validate(calForm, bindingResult);

      if (bindingResult.hasErrors()) {
        model.addAttribute("message", "Some parameters might be incorrect, please verify.");
      } else {
        model.addAttribute("message", "The calibration job is being processed.");
        runCalibration(cal);
      }

    } catch (Exception ex) {
      // if parsing fails
      model.addAttribute("message", "Please verify your input and try again.");
    }

    return "calibrate";
  }

  @Async
  private void runCalibration(Calibration cal) {

    try {          
     
      // load opencv
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

      GaSettings settings = new GaSettings();

      // load param ranges
      settings.setLowerW(cal.getLower_w());
      settings.setUpperW(cal.getUpper_w());
      settings.setLowerWn(cal.getLower_w_n());
      settings.setUpperWn(cal.getUpper_w_n());
      settings.setLowerSigmaR(cal.getLower_s_r());
      settings.setUpperSigmaR(cal.getUpper_s_r());

      // load ga details
      settings.setTitle(cal.getTitle());
      settings.setDescription(cal.getDescription());
      settings.setMaxIndividuals(cal.getMax_ind());
      settings.setMaxGenerations(cal.getMax_gen());
      settings.setMutationPerc(cal.getMut_perc());

      // type settings
      settings.setMutationType(TypeUtils.getMutationType(cal.getMut_type()));
      settings.setCrossoverType(TypeUtils.getCrossoverType(cal.getCross_type()));
      settings.setFitnessFunction(TypeUtils.getFitnessType(cal.getFit_func()));
      settings.setSegmentationTechnique(TypeUtils.getSegmentationType(cal.getSeg_method()));

      /* generate the openCV matrices */      
      
      // for the original images
      ZipInputStream zisOrig = new ZipInputStream(cal.getOriginalImages().getInputStream());
      ZipEntry zeOrig;
      while ((zeOrig = zisOrig.getNextEntry()) != null) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(zisOrig, out);
        Mat imagen = new MatOfByte(out.toByteArray());
        settings.addToOriginalImages(imagen);
      }
      zisOrig.close();
      
      ZipInputStream zisGround = new ZipInputStream(cal.getGroundtruthImages().getInputStream());
      ZipEntry zeGround;
      while ((zeGround = zisGround.getNextEntry()) != null) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(zisGround, out);
        Mat imagen = new MatOfByte(out.toByteArray());
        settings.addToGroundtruthImages(imagen);
      }
      zisGround.close();
      
      // call and run the calibration GA algorithm
      GaCalibration gaCalibration = new GaCalibration(settings);
      gaCalibration.runCalibration();

    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }

  }



}
