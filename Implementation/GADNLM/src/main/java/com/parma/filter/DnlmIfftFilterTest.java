package com.parma.filter;

import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;
import org.opencv.core.Mat;
import com.parma.images.ImageHandler;

public class DnlmIfftFilterTest {

  @Test
  public void test() {
    ImageHandler ih = new ImageHandler();
    Mat matriz = ih.leerImagenGrises("C:\\Users\\Eliot\\Desktop\\FastDnlmv2\\lena.jpg");

    double w = 21;
    double w_n = 3;
    double sigma_r = 100;

    
    long temp = System.currentTimeMillis();

    
    DnlmIfftFilter myFilter = new DnlmIfftFilter(matriz, w, w_n, sigma_r);
    myFilter.filterImage();
    
    //assertTrue("Mismatch.", );
  }

}
