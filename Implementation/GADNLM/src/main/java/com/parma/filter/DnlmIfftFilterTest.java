package com.parma.filter;

import org.junit.Test;
import org.opencv.core.Mat;
import com.parma.images.ImageHandler;

public class DnlmIfftFilterTest {

  @Test
  public void test() {
    ImageHandler ih = new ImageHandler();
<<<<<<< HEAD
    Mat image = ih.leerImagenGrises("C:\\Users\\Joel\\Desktop\\FastDnlmv2\\lena.jpg");
=======
    Mat matriz = ih.leerImagenGrises("C:\\Users\\Eliot\\Desktop\\FastDnlmv2\\lena.jpg");
>>>>>>> 4e1ecc73e807cbcdb6702a6efdda000c9488f5f9

    double w = 21;
    double w_n = 3;
    double sigma_r = 100;

<<<<<<< HEAD
    long temp = System.currentTimeMillis();
    
    DnlmIfftFilter myFilter = new DnlmIfftFilter(image, w, w_n, sigma_r);
    myFilter.filterImage();
    Mat resultado = myFilter.getResult();
    double checkSum = 0;
    
    for (int i = 0; i < resultado.rows(); i++) {
      for (int j = 0; j < resultado.cols(); j++) {
        checkSum += resultado.get(i, j)[0];
      }
    }
    
    System.out.println(checkSum + "");
    
    Mat nueva = new Mat();
    image.copyTo(nueva);
   
    long end = System.currentTimeMillis();   
    
    System.out.println(end - temp);
=======
    
    long temp = System.currentTimeMillis();

    
    DnlmIfftFilter myFilter = new DnlmIfftFilter(matriz, w, w_n, sigma_r);
    myFilter.filterImage();
>>>>>>> 4e1ecc73e807cbcdb6702a6efdda000c9488f5f9
    
    //assertTrue("Mismatch.", );
  }

}
