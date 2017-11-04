package com.parma.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.parma.dal.CalibrationDal;
import com.parma.dal.ReportDal;
import com.parma.model.Calibration;
import com.parma.model.FitnessReport;
import com.parma.model.TimeReport;
import com.parma.utils.ReportUtils;

@Controller
public class ReportController {

  @RequestMapping(value = "/reports", method = RequestMethod.GET)
  public String reports(HttpServletRequest servletRequest, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    String bestFitnessCSV = "";
    String averageFitnessCSV = "";

    List<List<FitnessReport>> fitnessReports = new ArrayList<List<FitnessReport>>();
    List<String> reports = getReportList(servletRequest);
    for (String report : reports) {
      List<FitnessReport> fitnessReport = ReportDal.loadFitnessReports(report);
      fitnessReports.add(fitnessReport);

    }
    if (fitnessReports.size() > 0) {
      bestFitnessCSV = ReportUtils.bestFitnessReportsToCSV(fitnessReports);
      averageFitnessCSV = ReportUtils.averageFitnessReportsToCSV(fitnessReports);
    }

    String bestFitnessTable = ReportUtils.generateTable(bestFitnessCSV);
    String averageFitnessTable = ReportUtils.generateTable(averageFitnessCSV);

    model.addAttribute("chart1", bestFitnessTable);
    model.addAttribute("chart2", averageFitnessTable);

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
    System.out.println(getReportList(request));
    return "redirect:/reports";
  }


  @RequestMapping(value = "/report-clear", method = RequestMethod.POST)
  public String clearReports(HttpServletRequest request, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    request = clearReportList(request);

    return "redirect:/reports";
  }


  /*
   * Export files
   * 
   */

  @RequestMapping(value = "/export-best-csv", method = RequestMethod.POST)
  public void exportBestFitnessCSV(HttpServletRequest servletRequest, HttpServletResponse response,
      Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    try {

      String csvFile = "";
      List<List<FitnessReport>> fitnessReports = new ArrayList<List<FitnessReport>>();
      List<String> reports = getReportList(servletRequest);
      for (String report : reports) {
        List<FitnessReport> fitnessReport = ReportDal.loadFitnessReports(report);
        fitnessReports.add(fitnessReport);

      }
      if (fitnessReports.size() > 0) {
        csvFile = ReportUtils.bestFitnessReportsToCSV(fitnessReports);
      }

      response.setContentType("text/csv");
      response.setHeader("Content-Disposition", "attachment;filename=best_fitness.csv");
      ServletOutputStream out = response.getOutputStream();
      out.println(csvFile);
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "/export-mean-csv", method = RequestMethod.POST)
  public void exportAvgFitnessCSV(HttpServletRequest servletRequest, HttpServletResponse response,
      Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    try {


      String csvFile = "";
      List<List<FitnessReport>> fitnessReports = new ArrayList<List<FitnessReport>>();
      List<String> reports = getReportList(servletRequest);
      for (String report : reports) {
        List<FitnessReport> fitnessReport = ReportDal.loadFitnessReports(report);
        fitnessReports.add(fitnessReport);

      }
      if (fitnessReports.size() > 0) {
        csvFile = ReportUtils.averageFitnessReportsToCSV(fitnessReports);
      }

      response.setContentType("text/csv");
      response.setHeader("Content-Disposition", "attachment;filename=best_fitness.csv");
      ServletOutputStream out = response.getOutputStream();
      out.println(csvFile);
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @RequestMapping(value = "/export-best-tikz", method = RequestMethod.POST)
  public void exportBestFitnessTikz(HttpServletRequest servletRequest, HttpServletResponse response,
      Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    try {

      String tikzFile = "";
      List<List<FitnessReport>> fitnessReports = new ArrayList<List<FitnessReport>>();
      List<String> reports = getReportList(servletRequest);
      for (String report : reports) {
        List<FitnessReport> fitnessReport = ReportDal.loadFitnessReports(report);
        fitnessReports.add(fitnessReport);

      }
      if (fitnessReports.size() > 0) {
        tikzFile = ReportUtils.bestFitnessReportsToCSV(fitnessReports);
        tikzFile = ReportUtils.generateTikz(tikzFile, "Best Fitness");
      }

      response.setContentType("text/tex");
      response.setHeader("Content-Disposition", "attachment;filename=best_fitness.tex");
      ServletOutputStream out = response.getOutputStream();
      out.println(tikzFile);
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "/export-mean-tikz", method = RequestMethod.POST)
  public void exportAvgFitnessTikz(HttpServletRequest servletRequest, HttpServletResponse response,
      Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    try {

      String tikzFile = "";
      List<List<FitnessReport>> fitnessReports = new ArrayList<List<FitnessReport>>();
      List<String> reports = getReportList(servletRequest);
      for (String report : reports) {
        List<FitnessReport> fitnessReport = ReportDal.loadFitnessReports(report);
        fitnessReports.add(fitnessReport);

      }
      if (fitnessReports.size() > 0) {
        tikzFile = ReportUtils.averageFitnessReportsToCSV(fitnessReports);
        tikzFile = ReportUtils.generateTikz(tikzFile, "Mean of Fitness");
      }

      response.setContentType("text/tex");
      response.setHeader("Content-Disposition", "attachment;filename=mean_fitness.tex");
      ServletOutputStream out = response.getOutputStream();
      out.println(tikzFile);
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  
  /*
   * SVG exports
   */
  @RequestMapping(value = "/export-best-svg", method = RequestMethod.POST)
  public void exportBestFitnessSVG(HttpServletRequest servletRequest, HttpServletResponse response,
      @RequestParam("svg") String svg, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    try {      
      response.setContentType("image/svg+xml");
      response.setHeader("Content-Disposition", "attachment;filename=best_fitness.svg");
      ServletOutputStream out = response.getOutputStream();
      out.println(svg.split("</svg>")[0] + "</svg>");
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @RequestMapping(value = "/export-mean-svg", method = RequestMethod.POST)
  public void exportMeanFitnessSVG(HttpServletRequest servletRequest, HttpServletResponse response,
      @RequestParam("svg") String svg, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());
    try {      
      response.setContentType("image/svg+xml");
      response.setHeader("Content-Disposition", "attachment;filename=mean_fitness.svg");
      ServletOutputStream out = response.getOutputStream();
      out.println(svg.split("</svg>")[0] + "</svg>");
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Time Report export
   */

  @RequestMapping(value = "/export-time", method = RequestMethod.POST)
  public void exportTime(HttpServletRequest servletRequest, HttpServletResponse response,
      @RequestParam("calibration") String calibration, @RequestParam("type") String type,
      Model model) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    try {

      String csvFile = "";
      csvFile = type.toLowerCase() + ",time_elapsed\n";

      List<TimeReport> reports =
          ReportDal.loadTimeReports(calibration, (type.equals("TOTAL")) ? "GENERATION" : type);

      if (type.equals("TOTAL")) {
        long totalTime = 0;
        for (TimeReport row : reports) {
          totalTime += row.getTimeElapsed();
        }
        csvFile += "1," + String.valueOf(totalTime);
      } else {
        for (int i = 0; i < reports.size(); i++) {
          csvFile += (i + 1) + "," + reports.get(i).getTimeElapsed() + "\n";
        }
      }

      response.setContentType("text/csv");
      response.setHeader("Content-Disposition",
          "attachment;filename=" + calibration + "_" + type.toLowerCase() + "_report.csv");
      ServletOutputStream out = response.getOutputStream();
      out.println(csvFile);
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
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

    boolean valid = true;
    if (request.getSession().getAttribute("reports") != null) {
      List<String> list = (List<String>) request.getSession().getAttribute("reports");


      if (list.size() >= 1) {
        Calibration cal1 = CalibrationDal.loadCalibration(list.get(0));
        Calibration cal2 = CalibrationDal.loadCalibration(report);
        if (cal1.getMax_gen() != cal2.getMax_gen()) {
          valid = false;
        }
      }

      if (list.contains(report)) {
        valid = false;
      }

      if (valid) {
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
