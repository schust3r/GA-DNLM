package com.parma.filter;

import FastDnlmFilter.*;
import com.mathworks.toolbox.javabuilder.*;

public class DnlmIfftFilter {

  private FastDnlmFilter filter = null;
  private MWNumericArray out = null;
  private Object[] result = null;
  
  // parameters to be applied to the filter
  private double[][][] I; // image
  private double w, w_n, sigma_r; // calibration params
  
  public DnlmIfftFilter(double[][][] i, double w, double w_n, double sigma_r) {
    try {
      this.filter = new FastDnlmFilter();
      this.I = i;
      this.w = w;
      this.w_n = w_n;
      this.sigma_r = sigma_r;
      
      // apply filtering      
      filterImage();
      
    } catch (MWException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Apply filtering to specified image
   */
  public void filterImage() {
    try {      
      result = filter.fastNLMFilter(1, I, w, w_n, sigma_r);
      out = (MWNumericArray) result[0];  
      System.out.println(out);
    } catch (MWException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Getter for the DNLM-IFFT's result
   * 
   * @return the double array from the filtered image
   */
  public double[][] getResult() {
    return (double[][]) out.toDoubleArray();
    /*
    int fils = out.getDimensions()[0];
    int cols = out.getDimensions()[1];
    double[][] img = new double[fils][cols];
    for (int x = 0; x < fils; x++) {
      for (int y = 0; y < cols; y++) {
        img[x][y] = out.get
      }
    } 
    */    
  }
  
  
}
