package com.parma.segmentation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opencv.core.Mat;

import com.parma.images.ImageHandler;

public class OtsuTest {

	@Test
	public void test() {
		
		ImageHandler imageHandler = new ImageHandler();
		Mat imagen1  = imageHandler.leerImagenGrises("test_files/input/lena.jpg");
		int otsu = Otsu.getOtsusThreshold(imagen1);

		assertTrue("Resultado incorrecto.", otsu == 117);
	}

}
