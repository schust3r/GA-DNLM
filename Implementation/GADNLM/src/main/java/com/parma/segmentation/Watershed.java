package com.parma.segmentation;	

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Watershed {
	
	
	public static Mat getSegmentedImage(Mat imagen) {
		
		Mat bw = new Mat();
		
		Imgproc.cvtColor(imagen, bw, Imgproc.COLOR_RGB2GRAY);
		bw.convertTo(bw, CvType.CV_8UC1);
		
		Thresholding thresholder = new Thresholding();
		int threshold = Otsu.getOtsusThreshold(bw);
		thresholder.applyThreshold(bw, 50);
		
		Mat transform = new Mat();
		Imgproc.distanceTransform(bw, transform, Imgproc.CV_DIST_L2, 3);
		Core.normalize(transform, transform,0, 1.0, Core.NORM_MINMAX );

		Mat kernel = Mat.ones(3,3, CvType.CV_8UC1);
		Imgproc.dilate(transform, transform, kernel );
		Mat transform_8u = new Mat();
		transform.convertTo(transform_8u, CvType.CV_8U);
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(transform_8u, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		Mat markers = Mat.zeros(transform.size(), CvType.CV_32SC1);
	    for (int i = 0; i < contours.size(); i++) {
	        Imgproc.drawContours(markers, contours, i, Scalar.all(i+1), -1);
	    }
	    //Core.multiply(markers, new Scalar(10000), markers);
	    
	    
	    Imgproc.watershed(imagen, markers);
	    
	    
	    Mat mark = Mat.zeros(markers.size(), CvType.CV_8UC1);
	    markers.convertTo(mark, CvType.CV_8UC1);
	    Core.bitwise_not(mark, mark);
	    
	    Mat dst = Mat.zeros(markers.size(), CvType.CV_8UC1);
	    for(int i = 0 ; i < markers.rows(); i++) {
	    	for(int j = 0; j < markers.cols(); j++) {
	    		double pixel = markers.get(i, j)[0];
	    		if(pixel > 0 && pixel <= contours.size()) {
	    			dst.put(i, j, 255);
	    		}
	    	}
	    }
	    
	    return dst;
	    
	    
		
	}

}
