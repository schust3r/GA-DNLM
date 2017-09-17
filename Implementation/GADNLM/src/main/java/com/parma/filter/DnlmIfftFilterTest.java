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
    Mat matriz = ih.leerImagenColor("test_files/input/lena.jpg"); 
    
    System.out.println(matriz.channels());
    
    double[][][] image = new double[matriz.rows()][matriz.cols()][3];       
    
    for (int ch = 0; ch < 3; ch++) {
      for (int y = 0; y < matriz.height(); y++) {
        for (int x = 0; x < matriz.width(); x++) {          
          image[y][x][ch] = matriz.get(y, x)[0];
          if (ch == 0 && y == 0 && x == 0) System.out.print(image[y][x][ch] + " ");
        }
      }
    }       
    
    double w = 3;
    double w_n = 5;
    double sigma_r = 7;
        
    DnlmIfftFilter myFilter = new DnlmIfftFilter(image, w, w_n, sigma_r);
    
    double[][] expected = {{0,0,0}, {0,0,0}, {0,0,0}};
    
    assertTrue("Mismatch.", Arrays.equals(myFilter.getResult(), expected));
    
  }

}
