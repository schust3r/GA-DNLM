package tec.psa.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.opencv.core.Mat;

import tec.psa.segmentacion.Histograma;
import tec.psa.segmentacion.algoritmos.Kittler;
import tec.psa.segmentacion.conf.Const;
import tec.psa.segmentacion.imagenes.ImageHandler;

public class KittlerTest {

  @Test
  public void testCalcularUmbral() {

    // Cargar la imagen en escala de grises
    ImageHandler ih = new ImageHandler();
    Mat img = ih.leerImagenGrises("test_files/input/cuadro1_005.bmp");

    Histograma hist = new Histograma(img, Const.LIMITE);
    Kittler test = new Kittler(hist);

    int t = test.calcularUmbral();
    
    // Umbral T
    assertEquals("Umbral t no es igual", 167, t);
  }

}
