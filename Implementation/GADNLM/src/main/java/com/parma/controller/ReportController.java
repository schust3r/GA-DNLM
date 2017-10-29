package com.parma.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.parma.dal.CalibrationDal;
import com.parma.model.Calibration;

@Controller
public class ReportController {

  @RequestMapping(value = "/reports", method = RequestMethod.GET)
  public String reports(HttpServletRequest servletRequest, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    List<Calibration> calibrations = CalibrationDal.loadFinishedCalibrations();
    model.addAttribute("calibrations", calibrations);

    return "reports";
  }


  @RequestMapping(value = "/report-add", method = RequestMethod.POST)
  public String addReport(HttpServletRequest request, Model model,
      @RequestParam("calibration") String calibration) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    request = addReportToList(calibration, request);

    return "redirect:/reports";
  }

  
  @RequestMapping(value = "/report-clear", method = RequestMethod.POST)
  public String clearReports(HttpServletRequest request, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    request = clearReportList(request);
    
    return "redirect:/reports";
  }

  /**
   * Get string list with displayed reports.
   * 
   * @param request
   * @return
   */
  private List<String> getReportList(HttpServletRequest request) {
    if (request.getSession().getAttribute("reports") != null) {
      return (List<String>) request.getSession().getAttribute("reports");
    } else {
      return new ArrayList<String>();
    }
  }
  
  /**
   * Add another report to the string list.
   * 
   * @param report
   * @param request
   * @return
   */
  private HttpServletRequest addReportToList(String report, HttpServletRequest request) {
    if (request.getSession().getAttribute("reports") != null) {
      List<String> list = (List<String>) request.getSession().getAttribute("reports");
      if (!list.contains(report)) {
        list.add(report);      
        request.getSession().setAttribute("reports", list);
      }
    } else {
      request.getSession().setAttribute("reports", new ArrayList<String>(Arrays.asList(report)));      
    }    
    return request;
  }
  
  /**
   * Clear the report list.
   * 
   * @param request
   * @return
   */
  private HttpServletRequest clearReportList(HttpServletRequest request) {
    request.getSession().setAttribute("reports", null);
    return request;
  }

}
