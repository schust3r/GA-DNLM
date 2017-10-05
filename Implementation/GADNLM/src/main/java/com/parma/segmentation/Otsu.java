package com.parma.segmentation;


/**
 * Image Thresholding using Otsu's method, using OpenCV.
 * 
 * @author Credits to kfaRabi (github.com/kfaRabi)
 *
 */

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Otsu {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static long[] getHistogram(Mat inputImage) {
		inputImage.convertTo(inputImage, CvType.CV_8U);
		long histogram[] = new long[256];
		for (int r = 0; r < inputImage.rows(); r++) {
			for (int c = 0; c < inputImage.cols(); c++) {
				double intensity[] = inputImage.get(r, c);
				int value = (int) intensity[0];
				if(value > 255) System.out.println(value);
				
				histogram[value]++;
			}
		}
		return histogram;
	}

	public static int getOtsusThreshold(Mat inputImage) {
		long frequency[] = getHistogram(inputImage);
		double p[] = new double[frequency.length];
		long totalPixels = inputImage.rows() * inputImage.cols();

		for (int i = 0; i < frequency.length; i++) {
			p[i] = frequency[i] / ((double) totalPixels);
		}

		double mg = 0;
		for (int i = 0; i < frequency.length; i++) {
			mg += i * p[i];
		}
		mg /= 1;

		int threshold = 0;
		double bestSigma = Double.MIN_VALUE;
		for (int k = 0; k < frequency.length; k++) {
			double m1k = 0;
			double m2k = 0;
			double mk = 0;

			double p1k = 0;
			double p2k = 0;

			for (int i = 0; i <= k; i++) {
				p1k += p[i];
			}
			p2k = 1 - p1k;

			for (int i = 0; i <= k; i++) {
				m1k += i * p[i];
			}
			mk = m1k;
			m1k /= p1k;

			for (int i = k + 1; i < frequency.length; i++) {
				m2k += i * p[i];
			}
			m2k /= p2k;

			double sigmaSquareB = (mg * p1k - mk) * (mg * p1k - mk) / (p1k * p2k);
			
			if (sigmaSquareB > bestSigma) {
				bestSigma = sigmaSquareB;
				threshold = k;
			}
		}
		return threshold;
	}
	
}