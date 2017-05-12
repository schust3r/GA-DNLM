package tec.psa.segmentacion.algoritmos;

import org.opencv.core.Mat;

import org.opencv.core.Size;

/**
 * Clase para calcular la métrica de Dice o coeficiente  
 * de Sorensen-Dice. Permite encontrar qué tan similares 
 * son dos imágenes.
 * 
 *  Matematicamente - 2 * |A intersect B| / ( |A| + |B| )
 * 
 * @author Joel Barrantes
 *
 */
public class Dice {

  /**
   * Calcula el indice de similitud de Dice 
   * entre dos imagenes.
   * 
   * @param groundTruth Imagen segmentada manualmente
   * @param imagenUmbralizada Imagen segmentada con el tao de kittler
   * @return indice de Dice = 2 * interseccion/(cantidad de pixeles de las 2 imagenes)
   */
  
  public static double calcularDice(Mat groundTruth, Mat imagenUmbralizada) {
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

      double diceIndex = (2 * cardIntersection) / (sizeGroundTruth.area()
          + sizeImagenUmbralizada.area());
      return diceIndex;
    } else {
      
      return 0;

    }

  }
}