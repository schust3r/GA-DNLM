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

    String filename = "coins.jpg";

    Mat imagen = ih.leerImagenGrises("test_files/input/" + filename);
    DnlmFilter filter = new DnlmFilter();
    double mils = System.currentTimeMillis();

    Mat res = null;
    res = filter.filter(imagen, 3, 3, 5);

    System.out.println("Time of processing: " + (System.currentTimeMillis() - mils));
    ih.guardarImagen("test_files/output/", filename + "_out", "jpg", res);
    
    double checksum = Core.sumElems(res).val[0];
    System.out.printf("Checksum: %f\n", checksum);

    assertTrue("Resultado inaceptable.", checksum - 10827950.3390755 < 1750);

  }

}
