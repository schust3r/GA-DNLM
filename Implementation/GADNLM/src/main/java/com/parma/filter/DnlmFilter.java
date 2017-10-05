package com.parma.filter;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class DnlmFilter {

  public Mat filter(Mat I, double w, double w_n, double sigma_r) {

    Mat G = new Mat(I.size(), CvType.CV_64FC1);
    I.copyTo(G);
    G.convertTo(G, CvType.CV_64FC1);

    int size_x = G.rows();
    int size_y = G.cols();

    Mat R = Mat.zeros(size_x, size_y, CvType.CV_64FC1);       

    Mat II2 = Mat.zeros(new Size(size_x, size_y), CvType.CV_64FC1);
    Imgproc.integral(G.mul(G), II2, CvType.CV_64FC1);

    double sigma_s = w / 1.5;
    Mat X = new Mat(), Y = new Mat();
    Range r = new Range(new double[] {-w, w});
    meshgrid(r, r, X, Y);

    X = X.mul(X);
    Y = Y.mul(Y);
    Mat S = new Mat(X.size(), CvType.CV_64FC1);       
    Core.add(X, Y, S);
    Core.multiply(S, new Scalar(-1.0 / (2.0 * sigma_s * sigma_s)), S);
    
    System.out.println(S.dump());
    System.out.println();

    Mat GaussW = new Mat(S.size(), CvType.CV_64FC1);
    for (int i = 0; i < GaussW.width(); i++) {
      for (int j = 0; j < GaussW.height(); j++) {
        double pixel = Math.pow(Math.E, S.get(i, j)[0]);
        GaussW.put(i, j, pixel);
      }
    }
    
    System.out.println(GaussW.dump());
    System.out.println();

    Mat U = NoAdaptativeUSM(G, 3, 17, 0.005);
    
    System.out.println(U.dump());
    System.out.println();

    for (int i = 0; i < size_x; i++) {
      for (int j = 0; j < size_y; j++) {

        if ((i > w + w_n) && (j > w + w_n) && (i < size_x - w - w_n) && (j < size_y - w - w_n)) {
          // extract local
          int iMin = (int) Math.max(i - w - w_n, 0);
          int iMax = (int) Math.min(i + w + w_n, size_x - 1);
          int jMin = (int) Math.max(j - w - w_n, 0);
          int jMax = (int) Math.min(j + w + w_n, size_y - 1);
          // get current window
          I = G.submat(iMin - 1, iMax, jMin - 1, jMax);
          I.convertTo(I, CvType.CV_64FC1);
          
          System.out.println(I.dump());
          System.out.println();

          int sizeW_x = I.width();
          int sizeW_y = I.height();
          // create output matrix
          Mat O = Mat.zeros((int) (sizeW_x - 2 * w_n), (int) (sizeW_y - 2 * w_n), CvType.CV_64FC1);

          // extract pixel neighborhood P local region
          int mMin_p = (int) (i - w_n);
          int mMax_p = (int) (i + w_n);
          int nMin_p = (int) (j - w_n);
          int nMax_p = (int) (j + w_n);

          // get sum of squad neighborhood P
          double sum_p = II2.get(mMin_p, nMin_p)[0] + II2.get(mMax_p + 1, nMax_p + 1)[0]
              + II2.get(mMin_p, nMax_p + 1)[0] + II2.get(mMax_p + 1, nMin_p)[0];

          // get current neighborhood p
          Mat neighbor_p = G.submat(mMin_p, mMax_p, nMin_p, nMax_p);
          neighbor_p.convertTo(neighbor_p, CvType.CV_64FC1);
          
          System.out.println(neighbor_p.dump());
          System.out.println();
              
          int sizeP_x = neighbor_p.width();
          int sizeP_y = neighbor_p.height();

          // perform correlation
          // output size
          int mm = sizeW_x + sizeP_x - 1;
          int nn = sizeW_y + sizeP_y - 1;
          // pad, multiply and transform back
          Mat C_a = Mat.zeros(new Size(mm, nn), CvType.CV_64FC1);
          Mat C_b = Mat.zeros(new Size(mm, nn), CvType.CV_64FC1);
          Core.copyMakeBorder(I, I, 0, nn - sizeW_y, 0, mm - sizeW_x, Core.BORDER_CONSTANT);
          Core.dft(I, C_a);
          Core.copyMakeBorder(neighbor_p, neighbor_p, 0, nn - sizeP_y, 0, mm - sizeP_x,
              Core.BORDER_CONSTANT);
          Core.dft(neighbor_p, C_b);
          Core.flip(C_b, C_b, -1); // rot90 two times
          C_b = C_b.mul(C_a);
          Mat C = new Mat(new Size(mm, nn), CvType.CV_64FC1);
          Core.dft(C_b, C, Core.DFT_INVERSE, 0);
          // padding constants (for output of size == size(A))
          int padC_m = (int) Math.ceil((sizeP_x - 1) / 2.0);
          int padC_n = (int) Math.ceil((sizeP_y - 1) / 2.0);
          // convolution result
          Mat correlation = C.submat(padC_m + 1, sizeW_x + padC_m, padC_n + 1, sizeW_y + padC_n);
          
          System.out.println(correlation.dump());
          System.out.println();

          for (int m = (int) w_n; m < sizeW_x - w_n; m++) {
            int mMin_w = (int) (iMin + m - 1 - w_n);
            int mMax_w = (int) (iMin + m - 1 + w_n);

            for (int n = (int) w_n; n < sizeW_y - w_n; n++) {
              int nMin_w = (int) (jMin + n - 1 - w_n);
              int nMax_w = (int) (jMin + n - 1 + w_n);
              double sum_w = II2.get(mMin_w, nMin_w)[0] + II2.get(mMax_w + 1, nMax_w + 1)[0]
                  - II2.get(mMin_w, nMax_w + 1)[0] - II2.get(mMax_w + 1, nMin_w)[0];
              O.put((int) (m - w_n), (int) (n - w_n), sum_p + sum_w - 2 * correlation.get(m, n)[0]);
            }
          }

          for (int k0 = 0; i < O.width(); i++) {
            for (int k1 = 0; j < O.height(); j++) {
              double pixel = Math.pow(Math.E, O.get(i, j)[0] / (-2.0 * sigma_r * sigma_r));
              O.put(k0, k1, pixel);
            }
          }
          O = O.mul(GaussW);
          
          System.out.println(O.dump());
          System.out.println();
          
          double norm_factor = Core.sumElems(O).val[0];
          Mat OxU_sub = U.submat((int) (iMin + w_n - 1), (int) (iMax - w_n), (int) (jMin + w_n - 1),
              (int) (jMax - w_n));
          
          System.out.println(OxU_sub.dump());
          System.out.println();
          
          OxU_sub = OxU_sub.mul(O);
          
          System.out.println(OxU_sub.dump());
          System.out.println();
          
          Core.multiply(OxU_sub, new Scalar(1.0/norm_factor), OxU_sub);
          R.put(i, j, Core.sumElems(OxU_sub).val[0]);
        }
      }
    }
    
    System.out.println(R.dump());
    System.out.println();
    
    return R;
  } // end DNLM-IFFT Filter

  /**
   * No adaptative laplacian. CURRENT VERSION HAS HARDCODED KERNEL
   */
  private Mat NoAdaptativeUSM(Mat SrcImage, double lambda, int kernelSize, double kernelSigma) {
    Mat kernel = new Mat(kernelSize, kernelSize, CvType.CV_64FC1);
    for (int i = 0; i < kernelSize; i++) {
      for (int j = 0; j < kernelSize; j++) {
        double value = (i == 8 && j == 8) ? 
            79723.18339100346 : -276.8166089965398;
        kernel.put(i, j, value);
      }
    }
    Mat Z = new Mat(SrcImage.size(), CvType.CV_64FC1);    
    Imgproc.filter2D(SrcImage, Z, -1, kernel, new Point(-1, -1), 0.0, Core.BORDER_CONSTANT);
        
    System.out.println(Z.dump());
    System.out.println();    
    
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
    Mat U = new Mat(SrcImage.size(), CvType.CV_64FC1);
    Core.add(SrcImage, Z, U, Mat.ones(SrcImage.size(), CvType.CV_8UC1), CvType.CV_64FC1);
    return U;
  } // end NoAdaptativeUSM

  private void meshgrid(Range xgv, Range ygv, Mat X, Mat Y) {
    double[] t_x = new double[xgv.size() + 1];
    double[] t_y = new double[ygv.size() + 1];
    for (int i = xgv.start; i <= xgv.end; i++) {
      int index = i - xgv.start;
      t_x[index] = i;
    }
    for (int i = ygv.start; i <= ygv.end; i++) {
      int index = i - ygv.start;
      t_y[index] = i;
    }
    Mat xgvM = new Mat(new Size(1, t_x.length), CvType.CV_64FC1);
    xgvM.put(0, 0, t_x);
    Mat ygvM = new Mat(new Size(1, t_y.length), CvType.CV_64FC1);
    ygvM.put(0, 0, t_y);
    Core.repeat(xgvM.reshape(1, 1), (int) ygvM.total(), 1, X);
    Core.repeat(ygvM.reshape(1, 1).t(), 1, (int) xgvM.total(), Y);
  } // end Meshgrid

} /* end DnlmFilter */


