package tec.psa.segmentacion;

import org.opencv.core.Mat;

/**
 * Histograma a partir de las matrices de imágenes.
 * 
 * @author Joel Schuster
 *
 */
public class Histograma {

  // Histograma de valores enteros
  private int[] histo;

  /**
   * Constructor.
   * 
   * @param img matriz de la imagen
   * @param lim rango máximo del histograma
   */
  public Histograma(Mat img, int lim) {
    this.histo = new int[lim];
    obtenerHistograma(img);
  }

  /**
   * Calcula histograma de la imagen recibida.
   * 
   * @param img matriz de la imagen
   */
  public void obtenerHistograma(Mat img) {

    for (int y = 0; y < img.height(); y++) {
      for (int x = 0; x < img.width(); x++) {

        // Leer un "pixel" de la matriz
        double[] datosPixel = img.get(y, x);
        int escalaGris = (int)datosPixel[0];

        // Incrementar el valor de gris en el histograma
        histo[escalaGris]++;
      }
    }
  }

  /**
   * Getter para histograma.
   * 
   * @return histograma de la matriz de imagen 
   */
  public int[] getHistograma() {
    return histo;
  }

}
