package tec.psa.segmentacion.algoritmos;

import org.opencv.core.Mat;

import tec.psa.segmentacion.imagenes.ImageHandler;

public class EtiquetadoTest {

  /**
   * Prueba unitaria para el etiquetado de las celulas.
   */
  public void etiquetadoTest() {
    ImageHandler ih = new ImageHandler();
    Mat img = ih.leerImagenGrises("C:\\Users\\Reggie Barker\\Documents\\AC\\salida.bmp");
    Etiquetado et = new Etiquetado();
    ih.guardarImagen("C:\\Users\\Reggie Barker\\Documents\\AC", "EtiquetdoTestHOY", "bmp", et.etiquetarCelulas(img));
  }

}
