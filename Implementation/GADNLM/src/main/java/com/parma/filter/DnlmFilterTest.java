package com.parma.filter;

import static org.junit.Assert.*;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import com.parma.images.ImageHandler;

public class DnlmFilterTest {

  @Test
  public void test() {
    
    ImageHandler ih = new ImageHandler();
    Mat imagen = ih.leerImagenGrises("C:/Users/Joel/Desktop/TEMP/lena.jpg");
    
    DnlmFilter filter = new DnlmFilter();
    
    Mat res = filter.filter(imagen, 3, 4, 5);
    
    ih.guardarImagen("C:/Users/Joel/Desktop/TEMP", "dnlmLena", "jpg", res);
    
    System.out.println("Checksum: " + Core.sumElems(res).val[0]);        
    
  }

}
