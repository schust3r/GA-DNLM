package tec.psa.segmentacion.algoritmos;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opencv.core.Mat;

import tec.psa.segmentacion.Histograma;
import tec.psa.segmentacion.conf.Const;
import tec.psa.segmentacion.imagenes.ImageHandler;

public class KittlerTest {

  @Test
  public void testCalcularUmbral() {

    // Cargar la imagen en escala de grises
    ImageHandler ih = new ImageHandler();
    Mat img = ih.leerImagenGrises(
        "C:/Users/Joel/Dropbox/I Semestre 2017/Aseguramiento de la Calidad del Software/Proyectos/Prueba unitaria/cuadro1_005.bmp");

    Histograma hist = new Histograma(img, Const.LIMITE);
    Kittler test = new Kittler(hist);

    test.calcularUmbral();

    // Umbral T
    assertEquals("Umbral t no es igual", 168, test.getTao());

    // u_1
    assertTrue("u_1 es muy distinto", Math.abs(149.45 - test.mu0m) < 0.005);

    // u_2
    assertEquals("u_2 es muy distinto", Math.abs(219.49 - test.mu0m) < 0.005);

    // var_1
    assertEquals("var_1 es muy distinto", Math.abs(15.36 - test.mu0m) < 0.005);

    // var_2
    assertEquals("var_2 es muy distinto", Math.abs(10.05 - test.mu0m) < 0.005);

  }

}
