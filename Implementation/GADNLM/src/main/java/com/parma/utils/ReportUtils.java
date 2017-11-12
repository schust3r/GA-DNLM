package com.parma.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;
import com.parma.model.FitnessReport;

public class ReportUtils {

  public static String generateTable(String csv) {

    String table = "[";
    String[] csvLines = csv.split("\n");
    for (String line : csvLines) {
      table += ("[" + line + "],");
    }
    table = table.substring(0, table.length() - 1);
    table += ("]");

    return table;
  }

  public static String generateTikz(String csv, String yAxis) {

    String tikz = "\\definecolor{mycolor1}{rgb}{0.00000,0.44700,0.74100}%\n"
        + "\\definecolor{mycolor2}{rgb}{0.85000,0.32500,0.09800}%\n"
        + "\\definecolor{mycolor3}{rgb}{0.92900,0.69400,0.12500}%\n" + "\\begin{tikzpicture}\n"
        + "\n" + "\\begin{axis}[%\n" + "width=4.521in,\n" + "height=3.566in,\n"
        + "scale only axis,\n" + "xlabel={Generation},\n" + "ylabel={" + yAxis + "},\n"
        + "axis background/.style={fill=white},\n" + "legend pos=south east\n" + "]\n" + "\n"
        + "\\addplot [color=mycolor1]\n" + "  table[row sep=crcr]{\n";


    String[] csvLines = csv.split("\n");
    String[] header = csvLines[0].split(",");
    int cols = StringUtils.countOccurrencesOf(csvLines[0], ",") + 1;



    for (String line : csvLines) {
      if (line != csvLines[0]) {
        String[] terms = line.split(",");
        tikz += terms[0] + " " + terms[1] + "\\\\\n";
      }

    }

    tikz += "};\n\n" + "\\addlegendentry{" + header[1] + "}\n";

    if (cols == 3) {

      tikz += "\\addplot [color=mycolor2]\n" + "  table[row sep=crcr]{\n";
      for (String line : csvLines) {
        if (line != csvLines[0]) {
          String[] terms = line.split(",");
          tikz += terms[0] + " " + terms[2] + "\\\\\n";
        }

      }
      tikz += "};\n\n" + "\\addlegendentry{" + header[2] + "}\n";
    }

    if (cols == 4) {
      tikz += "\\addplot [color=mycolor3]\n" + "  table[row sep=crcr]{\n";
      for (String line : csvLines) {
        if (line != csvLines[0]) {
          String[] terms = line.split(",");
          tikz += terms[0] + " " + terms[3] + "\\\\\n";
        }

      }

      tikz += "};\n\n" + "\\addlegendentry{" + header[3] + "}\n";
    }



    tikz += "\\end{axis}\n" + "\\end{tikzpicture}\n";
    return tikz;



  }

  public static String bestFitnessReportsToCSV(List<List<FitnessReport>> fitnessReports) {

    List<String> csv = new ArrayList<String>();
    int maxGen = 0;
    csv.add("\"Generation\"");

    for (FitnessReport report : fitnessReports.get(0)) {
      maxGen = report.getGeneration();
      csv.add(String.valueOf(maxGen));
    }

    for (List<FitnessReport> report : fitnessReports) {
      csv.set(0, csv.get(0) + ",\"" + report.get(0).getCalibrationName() + "\"");
      for (int i = 0; i < maxGen; i++) {
        FitnessReport item = report.get(i);

        csv.set(i + 1, csv.get(i + 1) + "," + item.getBestScore());
      }
    }
    System.out.println("");
    String csvBuilder = "";
    for (String csvLine : csv) {
      csvBuilder += (csvLine + "\n");
    }
    csvBuilder = csvBuilder.substring(0, csvBuilder.length() - 1);
    return csvBuilder;
  }

  public static String averageFitnessReportsToCSV(List<List<FitnessReport>> fitnessReports) {
    List<String> csv = new ArrayList<String>();
    int maxGen = 0;
    csv.add("\"Generation\"");

    for (FitnessReport report : fitnessReports.get(0)) {
      maxGen = report.getGeneration();
      csv.add(String.valueOf(maxGen));
    }

    for (List<FitnessReport> report : fitnessReports) {
      csv.set(0, csv.get(0) + ",\"" + report.get(0).getCalibrationName() + "\"");
      for (int i = 0; i < maxGen; i++) {
        FitnessReport item = report.get(i);

        csv.set(i + 1, csv.get(i + 1) + "," + item.getAverageScore());
      }
    }
    System.out.println("");
    String csvBuilder = "";
    for (String csvLine : csv) {
      csvBuilder += (csvLine + "\n");
    }

    return csvBuilder;
  }

}
