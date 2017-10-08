package com.parma.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.parma.genetics.settings.GaSettings;
import com.parma.genetics.utils.TypeUtils;
import com.parma.model.Calibration;
import com.parma.model.User;
import com.parma.service.AsyncProcessService;
import com.parma.validator.CalibrationValidator;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

@Controller
public class CalibrateController {

  @Resource
  AsyncProcessService asyncService;

  @Autowired
  private CalibrationValidator calValidator;

  @RequestMapping(value = "/calibrate", method = RequestMethod.GET)
  public String dashboard(@ModelAttribute("user") User userForm, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    List<Calibration> calibrations = CalibrationDal.loadAllCalibrations();
    model.addAttribute("calibrations", calibrations);

    return "calibrate";
  }

  @RequestMapping(value = "/export-cal", params = {"id"}, method = RequestMethod.GET)
  public void exportCalibration(@RequestParam(value = "id") String title,
      HttpServletRequest servletRequest, HttpServletResponse response, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    try {
      Calibration cal = CalibrationDal.loadCalibration(title);
      String params = cal.getBest_w() + "," + cal.getBest_w_n() + "," + cal.getBest_s_r();
      response.setContentType("text/plain");
      response.setHeader("Content-Disposition", "attachment;filename=" + title + "_params.txt");
      ServletOutputStream out = response.getOutputStream();
      out.println(params);
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @RequestMapping(value = "/delete-cal", params = {"id"}, method = RequestMethod.GET)
  public String removeCalibration(@RequestParam(value = "id") String title,
      HttpServletRequest servletRequest, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    CalibrationDal.removeCalibration(title);
    model.addAttribute("message", "The calibration '" + title + "' has been deleted");

    return "redirect:/calibrate";
  }

  @RequestMapping(value = "/view-cal", params = {"id"}, method = RequestMethod.GET)
  public String viewCalibration(@RequestParam(value = "id") String title,
      HttpServletRequest servletRequest, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    // all calibrations
    List<Calibration> calibrations = CalibrationDal.loadAllCalibrations();
    model.addAttribute("calibrations", calibrations);
    // details of a single calibration
    Calibration cal = CalibrationDal.loadCalibration(title);
    model.addAttribute("cal", cal);

    return "calibrate";
  }

  @RequestMapping(value = "/run-calibration", method = RequestMethod.POST)
  public String runCalibration(HttpServletRequest request, Model model,
      @ModelAttribute("calibration") Calibration calForm, BindingResult bindingResult,
      @RequestParam("orig_images") MultipartFile[] origImages,
      @RequestParam("ground_images") MultipartFile[] groundImages) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    // load all calibrations
    List<Calibration> calibrations = CalibrationDal.loadAllCalibrations();
    model.addAttribute("calibrations", calibrations);

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

      cal.setOwner(auth.getName());

      // verify integrity of data
      calValidator.validate(cal, bindingResult);

      if (bindingResult.hasErrors()) {

        model.addAttribute("message", "Invalid parameters were found, please verify.");

      } else {

        model.addAttribute("message", "The calibration job is being processed.");

        // prepare calibration settings
        GaSettings settings = getCalibrationSettings(cal);

        // Save in database
        CalibrationDal.saveCalibration(cal);

        // call service to run asynchronously
        asyncService.processCalibration(settings);
      }

    } catch (Exception ex) {
      // if parsing fails
      System.out.println(ex.getMessage());
      model.addAttribute("message", "An unexpected error has occured, please try again.");
    }

    return "calibrate";
  }

  /**
   * Prepare settings for the genetic algorithm.
   * 
   * @param cal the calibration parameters
   */
  private GaSettings getCalibrationSettings(Calibration cal) {

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

    // map for the original images
    SortedMap<String, Mat> origImgMap = getNameMatrixMap(cal.getOriginalImages(), false);

    // map for the groundtruth images
    SortedMap<String, Mat> groundImgMap = getNameMatrixMap(cal.getGroundtruthImages(), true);

    settings.setOriginalImages(new ArrayList<Mat>(origImgMap.values()));
    settings.setGroundtruthImages(new ArrayList<Mat>(groundImgMap.values()));

    return settings;
  }

  /**
   * Generate the OpenCV matrices from a multipart file list.
   * 
   * @param files bytes to generate the images
   * @param applyThreshold indicate if binary threshold must be applied
   * @return a sorted map of filenames and opencv matrices
   */
  private SortedMap<String, Mat> getNameMatrixMap(MultipartFile[] files, boolean groundtruth) {
    try {
      // load opencv
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

      SortedMap<String, Mat> matList = new TreeMap<>();
      for (int i = 0; i < files.length; i++) {
        // check if filename is not in name list
        MultipartFile mpf = files[i];
        String filename = mpf.getOriginalFilename();
        // get bytes and create a Mat
        byte[] imgBytes = mpf.getBytes();
        Mat image = Imgcodecs.imdecode(new MatOfByte(imgBytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        // conversion to grayscale
        Mat imageGray = image.clone();
        if (imageGray.channels() > 2) {
          Imgproc.cvtColor(image, imageGray, Imgproc.COLOR_RGB2GRAY);
          if (!groundtruth) {
            imageGray.convertTo(imageGray, CvType.CV_64FC1);
          }
        }
        // must apply threshold for groundtruth images
        if (groundtruth) {
          Imgproc.threshold(imageGray, imageGray, 1, 256, Imgproc.THRESH_BINARY);
        }
        matList.put(filename, imageGray);
      }
      return matList;
    } catch (Exception ex) {
      return null;
    }
  }



}
