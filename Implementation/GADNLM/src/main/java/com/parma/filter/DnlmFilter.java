package com.parma.filter;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class DnlmFilter {

  public void filter(Mat I, double w, double w_n, double sigma_r) {
        
    int size_x = I.rows();
    int size_y = I.cols();
    
    Mat R = Mat.zeros(size_x,  size_y, CvType.CV_64FC1);
    
    
    
    
  }
  
}
