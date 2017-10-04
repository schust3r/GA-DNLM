package com.parma.filter;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class DnlmFilter {

  public Mat filter(Mat I, double w, double w_n, double sigma_r) {

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

        if ((i > w + w_n) && (j > w + w_n) && (i < size_x - w - w_n) && (j < size_y - w - w_n)) {
          // extract local
          int iMin = (int) Math.max(i - w - w_n, 1);
          int iMax = (int) Math.min(i + w + w_n, size_x);
          int jMin = (int) Math.max(j - w - w_n, 1);
          int jMax = (int) Math.min(j + w + w_n, size_y);
          // get current window
          I = G.submat(iMin, iMax, jMin, jMax);
          int sizeW_x = I.width();
          int sizeW_y = I.height();
          // create output matrix
          Mat O = Mat.zeros((int) (sizeW_x - 2 * w_n), (int) (sizeW_y - 2 * w_n), CvType.CV_64F);

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
          int sizeP_x = neighbor_p.width();
          int sizeP_y = neighbor_p.height();

          // perform correlation
          // output size
          int mm = sizeW_x + sizeP_x - 1;
          int nn = sizeW_y + sizeP_y - 1;
          // pad, multiply and transform back
          Mat C_a = Mat.zeros(new Size(mm, nn), CvType.CV_64F);
          Mat C_b = Mat.zeros(new Size(mm, nn), CvType.CV_64F);
          Core.copyMakeBorder(I, I, 0, nn - sizeW_y, 0, mm - sizeW_x, Core.BORDER_CONSTANT);
          Core.dft(I, C_a);
          Core.copyMakeBorder(neighbor_p, neighbor_p, 0, nn - sizeP_y, 0, mm - sizeP_x,
              Core.BORDER_CONSTANT);
          Core.dft(neighbor_p, C_b);
          Core.flip(C_b, C_b, -1); // rot90 two times
          C_b.mul(C_a);
          Mat C = new Mat(new Size(mm, nn), CvType.CV_64F);
          Core.dft(C_b, C, Core.DFT_INVERSE, 0);
          // padding constants (for output of size == size(A))
          int padC_m = (int) Math.ceil((sizeP_x - 1) / 2.0);
          int padC_n = (int) Math.ceil((sizeP_y - 1) / 2.0);
          // convolution result
          Mat correlation = C.submat(padC_m + 1, sizeW_x + padC_m, padC_n + 1, sizeW_y + padC_n);

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
              double pixel = Math.pow(Math.E, O.get(i, j)[0]/(-2.0 * sigma_r * sigma_r));
              O.put(k0, k1, pixel);
            }
          }
          O.mul(GaussW);
          double norm_factor = Core.sumElems(O).val[0];          
          Mat OxU_sub = U.submat((int)(iMin + w_n), (int)(iMax - w_n), (int)(jMin + w_n), (int)(jMax - w_n));
          OxU_sub.mul(O);
          Core.multiply(OxU_sub, new Scalar(1.0 / norm_factor), OxU_sub);
          R.put(i, j, Core.sumElems(OxU_sub).val[0]);
        }
      }
    }
    return R;
  } // end DNLM-IFFT Filter

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
  } // end Meshgrid

}

