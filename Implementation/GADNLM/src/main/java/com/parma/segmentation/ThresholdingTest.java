package com.parma.segmentation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opencv.core.Mat;

import com.parma.images.ImageHandler;

public class ThresholdingTest {

	@Test
	public void test() {
		
		ImageHandler imageHandler = new ImageHandler();
		
		Mat imagen1  = imageHandler.leerImagenGrises("test_files/input/gradient.png");	
		
		Mat imagen2  = imageHandler.leerImagenGrises("test_files/input/output.png");
		
		
		Thresholding thresholder = new Thresholding();
		thresholder.applyThreshold(imagen1, 127);
		
		boolean isEqual = true;
		
		for(int x = 0; x < imagen1.height();x++) {
			for(int y = 0; y < imagen1.width();y++) {
				double[] pix1 = imagen1.get(x, y);
				double[] pix2 = imagen2.get(x, y);
				if (pix1[0] != pix2[0]) {
					isEqual = false;
					break;
				}
			}
		}
		System.out.println(isEqual); 		
		assertTrue("Umbralización incorrecta.",isEqual);
	}

}
