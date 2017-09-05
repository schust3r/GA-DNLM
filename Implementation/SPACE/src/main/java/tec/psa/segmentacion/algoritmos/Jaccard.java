package tec.psa.segmentacion.algoritmos;

import org.opencv.core.Mat;

import org.opencv.core.Size;

/**
 * Clase para calcular la métrica de Jaccard para analizar la similitud entre
 * dos imágenes. <br>
 * Matematicamente - |A intersect B| / ( |A| + |B| - |A intersect B| )
 * 
 * @author Joel Barrantes
 *
 */
public class Jaccard {

  /**
   * Calcula el coeficiente de similitud de Jaccard entre dos imagenes.
   * 
   * @param groundTruth
   *          Imagen segmentada manualmente
   * @param imagenUmbralizada
   *          Imagen segmentada por kittler
   * @return Indice de Jaccard
   */
  public static double calcularJaccard(Mat groundTruth, Mat imagenUmbralizada) {

    Size sizeGroundTruth = groundTruth.size();
    Size sizeImagenUmbralizada = imagenUmbralizada.size();

    if (sizeGroundTruth.equals(sizeImagenUmbralizada)) {

      int cardIntersection = 0;
      int limImagenX = (int) sizeGroundTruth.width;
      int limImagenY = (int) sizeGroundTruth.height;

      for (int y = 0; y < limImagenY; y++) {
        for (int x = 0; x < limImagenX; x++) {
          double[] primer = imagenUmbralizada.get(y, x);

          double[] segundo = groundTruth.get(y, x);
          // Leer un "pixel" de la matriz
          if (primer[0] == segundo[0]) {
            cardIntersection = cardIntersection + 1;

          }
        }
      }
      return (cardIntersection) / (sizeGroundTruth.area() + sizeImagenUmbralizada.area() - cardIntersection);
    } else {
      return 0;
    }
  }
}