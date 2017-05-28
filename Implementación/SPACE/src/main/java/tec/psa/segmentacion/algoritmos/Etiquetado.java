package tec.psa.segmentacion.algoritmos;

import java.awt.Color;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import org.opencv.core.MatOfPoint;

import org.opencv.core.Scalar;

import org.opencv.imgproc.Imgproc;

/**
 * Clase para realizar el etiquetado de cada una de las células segmentadas por
 * el umbral de Kittler. Etiqueta cada una con un color único. <br>
 * Referencia OpenCV - http://docs.opencv.org/
 * 
 * @author Reggie Barker
 *
 */

public class Etiquetado {

  /**
   * Segmenta una imagen de celulas previamente umbralizada.
   *
   * @param imagen
   *          la imagen umbralizada
   * @return Imagen segmentada
   */
  public Mat etiquetarCelulas(Mat imagen) {

    try {

      if (imagen == null) {
        throw new NullPointerException("Imagen de entrada no encontrada.");
      }

      Mat imagenParaEtiquetar = new Mat();

      Imgproc.cvtColor(imagen, imagenParaEtiquetar, Imgproc.COLOR_GRAY2RGB);
      ArrayList<MatOfPoint> contornos = new ArrayList<MatOfPoint>();
      Mat hierarchy = new Mat();
      Imgproc.findContours(imagen, contornos, hierarchy, 
          Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

      int colorBase = 100;
      int aumento = 900000 / contornos.size();

      // Recorre los puntos del contorno y los dibuja
      for (MatOfPoint matOfPoint : contornos) {

        Color colorCont = new Color(colorBase);
        ArrayList<MatOfPoint> c = new ArrayList<MatOfPoint>();
        c.add(matOfPoint);
        Scalar color = new Scalar(colorCont.getRed(), colorCont.getGreen(), colorCont.getBlue());
        Imgproc.drawContours(imagenParaEtiquetar, c, -1, color, -1);
        colorBase += aumento;

      }

      return imagenParaEtiquetar;
    } catch (NullPointerException ex) {
      ex.printStackTrace();
      return null;
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Obtiene el conteo de las células en una imagen.
   * 
   * @param imagen
   *          matriz de imagen en escala de grises
   * @return numero de celulas u objetos encontrados en la imagen
   */
  public long getConteoCelulas(Mat imagen) {
    try {
      Mat imagenParaEtiquetar = new Mat();

      if (imagen == null) {
        throw new NullPointerException("Imagen nula: no se pueden contar células.");
      }

      Imgproc.cvtColor(imagen, imagenParaEtiquetar, Imgproc.COLOR_GRAY2RGB);
      ArrayList<MatOfPoint> contornos = new ArrayList<MatOfPoint>();
      Mat hierarchy = new Mat();
      Imgproc.findContours(imagen, contornos, hierarchy, 
          Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
      return contornos.size();

    } catch (NullPointerException ex) {
      ex.printStackTrace();
      return 0;
    } catch (Exception ex) {
      ex.printStackTrace();
      return 0;
    }

  }

}