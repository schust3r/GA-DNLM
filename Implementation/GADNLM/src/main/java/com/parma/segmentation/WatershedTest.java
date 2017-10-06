package com.parma.segmentation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import com.parma.images.ImageHandler;

public class WatershedTest {

	@Test
	public void test() {


		ImageHandler ih = new ImageHandler();

	    String filename = "thrash.png";

	    Mat imagen = ih.leerImagenColor("test_files/input/" + filename);
	    
	    
	    Mat markers = Watershed.getSegmentedImage(imagen);
	    
	    //Core.multiply(markers, new Scalar(255), markers);
	    
	    ih.guardarImagen("C:\\Users\\Eliot\\Desktop", filename + "_out", "jpg", markers);
	    
	    
	    assertTrue(true);
		
	}

}
