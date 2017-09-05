package tec.psa.segmentacion.algoritmos;

import org.opencv.core.Mat;

import org.opencv.imgproc.Imgproc;

import tec.psa.segmentacion.conf.Const;

/**
 * Clase experta en umbralización de las imágenes. Referencia OpenCV -
 * http://docs.opencv.org/
 * 
 * @author Joel Barrantes
 *
 */
public class Umbralizacion {

  /**
   * Constructor
   */
  public Umbralizacion() {

  }

  /**
   * Umbraliza la imagen con OpenCV de forma binaria
   * 
   * @param imagen
   *          matriz OpenCV de la imagen
   * @param t
   *          valor de umbral para definir blancos y negros
   */
  public void aplicarUmbral(Mat imagen, int t) {
    Imgproc.threshold(imagen, imagen, t, Const.LIMITE, Imgproc.THRESH_BINARY);
  }

  /**
   * Umbraliza la imagen con OpenCV de forma binaria e inversa
   * 
   * @param imagen
   *          matriz OpenCV de la imagen
   * @param t
   *          valor de umbral para definir blancos y negros
   */
  public void aplicarUmbralInverso(Mat imagen, int t) {
    Imgproc.threshold(imagen, imagen, t, Const.LIMITE, Imgproc.THRESH_BINARY_INV);
  }

}