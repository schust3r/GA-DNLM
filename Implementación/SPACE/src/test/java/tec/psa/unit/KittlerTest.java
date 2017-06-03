package tec.psa.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Mat;

import tec.psa.segmentacion.Histograma;
import tec.psa.segmentacion.algoritmos.Kittler;
import tec.psa.segmentacion.conf.Const;
import tec.psa.segmentacion.imagenes.ImageHandler;

public class KittlerTest {

  private Histograma hist; 
  
  @Before
  public void setUp() {        
    ImageHandler ih = new ImageHandler();
    Mat img = ih.leerImagenGrises("test_files/input/cuadro1_005.bmp");
    hist = new Histograma(img, Const.LIMITE);
  }
  
  @Test
  public void testCalcularUmbral() {
    Kittler test = new Kittler(hist);        
    assertEquals("Umbral t no es igual", 168, test.calcularUmbral());
  }

}
