package com.parma.filter;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class DnlmFilter {

  public void filter(Mat I, double w, double w_n, double sigma_r) {

    Mat G = new Mat();
    I.convertTo(G, CvType.CV_64F);

    int size_x = I.rows();
    int size_y = I.cols();

    Mat R = Mat.zeros(size_x, size_y, CvType.CV_64F);

    Mat II2 = new Mat();
    Imgproc.integral(II2, G.mul(G));

    double sigma_s = w / 1.5;
    Mat X = new Mat(), Y = new Mat();
    Range r = new Range(new double[] {-w, w});
    meshgrid(r, r, X, Y);

    X.mul(X);
    Y.mul(Y);
    Mat S = new Mat();
    S.push_back(X);
    S.push_back(Y);
    Core.multiply(S, new Scalar(-1.0 / (2.0 * Math.pow(sigma_s, 2))), S);

    Mat GaussW = new Mat(S.width(), S.height(), 1);

    for (int i = 0; i < GaussW.width(); i++) {
      for (int j = 0; j < GaussW.height(); j++) {
        double pixel = Math.pow(Math.E, GaussW.get(i, j)[0]);
        GaussW.put(i, j, pixel);
      }
    }
    
    Mat U = NoAdaptativeUSM(G, 3, 17, 0.005);

    for (int i = 0; i < size_x; i++) {
      for (int j = 0; j < size_y; j++) {
        
      }
    }

  }

  /**
   * No adaptative laplacian
   */
  private Mat NoAdaptativeUSM(Mat SrcImage, double lambda, double kernelSize, double kernelSigma) {
    Mat kernel = new Mat(17, 17, 1);
    for (int i = 0; i < kernel.width(); i++) {
      for (int j = 0; j < kernel.height(); j++) {
        kernel.put(i, j, -276.8166);
      }
    }
    Mat Z = new Mat();
    Imgproc.filter2D(Z, SrcImage, 1, kernel);
    double maxZ = 0, maxSrc = 0;
    // get max (abs) of Z
    for (int i = 0; i < Z.width(); i++) {
      for (int j = 0; j < Z.height(); j++) {
        double currVal = Math.abs(Z.get(i, j)[0]);
        if (currVal > maxZ)
          maxZ = currVal;
      }
    }
    // get max of SrcImage
    for (int i = 0; i < SrcImage.width(); i++) {
      for (int j = 0; j < SrcImage.height(); j++) {
        double currVal = SrcImage.get(i, j)[0];
        if (currVal > maxSrc)
          maxSrc = currVal;
      }
    }
    Core.multiply(Z, new Scalar(lambda * maxSrc / maxZ), Z);  
    Mat U = new Mat();
    Core.add(SrcImage, Z, U);
    return U;
  } // end NoAdaptativeUSM

  private void meshgrid(Range xgv, Range ygv, Mat X, Mat Y) {
    int[] t_x = new int[xgv.size()];
    int[] t_y = new int[ygv.size()];
    for (int i = xgv.start; i <= xgv.end; i++) {
      t_x[i] = i;
    }
    for (int i = ygv.start; i <= ygv.end; i++) {
      t_y[i] = i;
    }
    Mat xgvM = new Mat();
    xgvM.put(1, 0, t_x);
    Mat ygvM = new Mat();
    ygvM.put(1, 0, t_y);
    Core.repeat(xgvM.reshape(1, 1), (int) ygvM.total(), 1, X);
    Core.repeat(xgvM.reshape(1, 1).t(), 1, (int) xgvM.total(), Y);
  }

}
