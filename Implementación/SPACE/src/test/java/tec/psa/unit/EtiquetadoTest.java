package tec.psa.unit;

import org.junit.Test;
import org.opencv.core.Mat;

import tec.psa.segmentacion.algoritmos.Etiquetado;
import tec.psa.segmentacion.imagenes.ImageHandler;

public class EtiquetadoTest {

  /**
   * Prueba unitaria para el etiquetado de las celulas.
   */
  @Test
  public void etiquetadoTest() {
    ImageHandler ih = new ImageHandler();
    Mat img = ih.leerImagenGrises("C:\\Users\\Reggie Barker\\Documents\\AC\\salida.bmp");
    Etiquetado et = new Etiquetado();
    ih.guardarImagen("files\\", "EtiquetdoTestHOY", "bmp", et.etiquetarCelulas(img));
  }

}
