package com.parma.filter;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import com.parma.images.ImageHandler;
import com.parma.segmentation.Otsu;
import com.parma.segmentation.Thresholding;

public class DnlmFilterTest {

  @Test
  public void test(){
    
    ImageHandler ih = new ImageHandler();
    
    String filename= "coins.tif";
    
    Mat imagen = ih.leerImagenGrises("test_files/input/"+filename);
    DnlmFilter filter = new DnlmFilter();
    double mils = System.currentTimeMillis();
    
  
	
	Mat res = null;
	res = filter.filter(imagen, 3, 3, 5);

	
    System.out.println("Time of processing: "+( System.currentTimeMillis()-mils));
    ih.guardarImagen("C:/Users/Eliot/Desktop", filename+"_out", "png", res);
    
    
    
   
    System.out.println("Checksum: " + Core.sumElems(res).val[0]);        
    
  }

}
