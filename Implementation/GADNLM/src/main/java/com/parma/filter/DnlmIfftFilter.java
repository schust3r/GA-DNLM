package com.parma.filter;

import FastDnlmFilter.*;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import com.mathworks.toolbox.javabuilder.*;

public class DnlmIfftFilter {

  private FastDnlmFilter filter;
  private MWNumericArray out;
  private Object[] result;
  
  // the image
  private double[][] I;
  
  // parameters calibration
  private double w, w_n, sigma_r;
  
  // matrix dimensions
  private int filas, cols;
  
  public DnlmIfftFilter(Mat i, double w, double w_n, double sigma_r) {
    try {
      this.filter = new FastDnlmFilter();
      this.I = mat2DoubleArray(i);
      this.w = w;
      this.w_n = w_n;
      this.sigma_r = sigma_r;      
    } catch (MWException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 
   * @param m
   * @return
   */
  private double[][] mat2DoubleArray(Mat m) {
    this.filas = m.rows();
    this.cols = m.cols();
    double[][] res = new double[m.rows()][m.cols()]; 
    for (int y = 0; y < filas; y++) {
      for (int x = 0; x < cols; x++) {
        res[y][x] = m.get(y, x)[0];
      }      
    }
    return res;
  }
  
  /**
   * Apply filtering to specified image
   */
  public void filterImage() {
    try {      
      result = filter.fastNLMFilter(1, I, w, w_n, sigma_r);
      out = (MWNumericArray) result[0];
    } catch (MWException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Getter for the DNLM-IFFT's result
   * 
   * @return the double array from the filtered image
   */
  public Mat getResult() {
    int[] dim = out.getDimensions();    
    Mat res = Mat.zeros(dim[0], dim[1], CvType.CV_64FC1); 
    double[][] outDouble = (double[][]) out.toDoubleArray(); 
    for (int x = 0; x < dim[0]; x++) {
      for (int y = 0; y < dim[1]; y++) {
        res.put(x, y, outDouble[x][y]);
      }
    }
    return res; 
  }
  
  
}
