package com.parma.segmentation;

import org.opencv.core.Mat;

import org.opencv.imgproc.Imgproc;

/**
 * Clase experta en umbralización de las imágenes. Referencia OpenCV -
 * http://docs.opencv.org/
 * 
 * @author Joel Barrantes
 *
 */
public class Thresholding {

  /**
   * Constructor
   */
  public Thresholding() {

  }

  /**
   * Umbraliza la imagen con OpenCV de forma binaria
   * 
   * @param imagen
   *          matriz OpenCV de la imagen
   * @param t
   *          valor de umbral para definir blancos y negros
   */
  public void applyThreshold(Mat imagen, int t) {
    Imgproc.threshold(imagen, imagen, t, 256, Imgproc.THRESH_BINARY);
  }

  /**
   * Umbraliza la imagen con OpenCV de forma binaria e inversa
   * 
   * @param imagen
   *          matriz OpenCV de la imagen
   * @param t
   *          valor de umbral para definir blancos y negros
   */
  public void applyInverseThreshold(Mat imagen, int t) {
    Imgproc.threshold(imagen, imagen, t, 256, Imgproc.THRESH_BINARY_INV);
  }

}