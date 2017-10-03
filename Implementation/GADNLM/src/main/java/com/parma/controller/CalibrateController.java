package com.parma.controller;

import java.util.ArrayList;
import java.util.concurrent.Future;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
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
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

@Controller
public class CalibrateController { 
  
  @Autowired
  private CalibrationValidator calValidator;
  
  private boolean calibrationStarted;

  @RequestMapping(value = "/calibrate", method = RequestMethod.GET)
  public String dashboard(@ModelAttribute("user") User userForm, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    return "calibrate";
  }

  @RequestMapping(value = "/run-calibration", method = RequestMethod.POST)
  public String getCalibrationSettings(HttpServletRequest request, Model model,
      @ModelAttribute("calibration") Calibration calForm, BindingResult bindingResult,
      @RequestParam("orig_images") MultipartFile[] origImages,
      @RequestParam("ground_images") MultipartFile[] groundImages) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    Calibration cal = new Calibration();
    calibrationStarted = false;

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
        runCalibration(cal);
        if (calibrationStarted) {
          model.addAttribute("message", "The calibration job is being processed.");          
        } else {
          model.addAttribute("message", "The calibration job has failed to start.");
        }
      }      

    } catch (Exception ex) {
      // if parsing fails
      model.addAttribute("message", "An unexpected error has occured, try again.");
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

      // Validity verification, since orig and ground images must match
      boolean imagesPaired = true;
      ArrayList<String> names = new ArrayList<String>();
      ArrayList<String> groundNames = new ArrayList<String>();

      /* for the original images */
      for (int i = 0; i < cal.getOriginalImages().length; i++) {
        // check if filename is not in name list
        MultipartFile mpf = cal.getOriginalImages()[i];
        String filename = mpf.getOriginalFilename();
        if (names.contains(filename)) {
          imagesPaired = false;
          break;
        }
        names.add(filename);
        // get bytes and create a Mat
        byte[] imgBytes = mpf.getBytes();
        Mat image = Imgcodecs.imdecode(new MatOfByte(imgBytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        settings.addToOriginalImages(image);
      }

      /* for the groundtruth images */
      for (int i = 0; i < cal.getGroundtruthImages().length; i++) {
        // verify correct filenames
        MultipartFile mpf = cal.getOriginalImages()[i];
        String filename = mpf.getOriginalFilename();
        if (!names.contains(filename) || groundNames.contains(filename)) {
          imagesPaired = false;
          break;
        }
        groundNames.add(filename);
        // get bytes and create a Mat
        byte[] imgBytes = mpf.getBytes();
        Mat image = Imgcodecs.imdecode(new MatOfByte(imgBytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        settings.addToGroundtruthImages(image);
      }
      
      // check equal size of groundtruth and source image arrays
      if (names.size() == groundNames.size()) {
        imagesPaired = false;
      }

      if (imagesPaired) {
        // call and run the calibration GA algorithm if correct
        calibrationStarted = true;
        GaCalibration gaCalibration = new GaCalibration(settings);        
        gaCalibration.runCalibration();
      } else {
        calibrationStarted = false;        
      }

    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }

  }



}
