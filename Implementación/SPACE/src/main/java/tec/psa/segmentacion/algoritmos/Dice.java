package tec.psa.segmentacion.algoritmos;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import tec.psa.segmentacion.conf.Const;

/**
 * Clase para calcular la métrica de Dice o coeficiente de Sorensen-Dice.
 * Permite encontrar qué tan similares son dos imágenes. <br>
 * Matematicamente - 2 * |A intersect B| / ( |A| + |B| )
 * 
 * @author Joel Barrantes
 *
 */
public class Dice {

  /**
   * Calcula el indice de similitud de Dice entre dos imagenes.
   * 
   * @param groundTruthBytes
   *          Imagen segmentada manualmente
   * @param imagenUmbralizadaBytes
   *          Imagen segmentada con el tao de kittler
   * @return indice de Dice = 2 * interseccion/(cantidad de pixeles de las 2
   *         imagenes)
   */
  public static double calcularDice(byte[] groundTruthBytes, byte[] imagenUmbralizadaBytes) {

    // llamar librería nativa
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    Mat imagenUmbralizada = Imgcodecs.imdecode(new MatOfByte(imagenUmbralizadaBytes),
        Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
    Size sz = imagenUmbralizada.size();

    Mat groundTruth = 
        Imgcodecs.imdecode(new MatOfByte(groundTruthBytes), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

    Imgproc.threshold(imagenUmbralizada, imagenUmbralizada, 1, Const.LIMITE, Imgproc.THRESH_BINARY);

    Imgproc.threshold(groundTruth, groundTruth, 1, Const.LIMITE, Imgproc.THRESH_BINARY);

    Size sizeGroundTruth = groundTruth.size();
    Size sizeImagenUmbralizada = imagenUmbralizada.size();

    if (!sizeGroundTruth.equals(sizeImagenUmbralizada)) {
      Imgproc.resize(groundTruth, groundTruth, sz, 0, 0, Imgproc.INTER_CUBIC);
    }

    int cardIntersection = 0;
    int limImagenX = (int) sizeGroundTruth.width;
    int limImagenY = (int) sizeGroundTruth.height;

    for (int y = 0; y < limImagenY; y++) {
      for (int x = 0; x < limImagenX; x++) {
        double[] primer = imagenUmbralizada.get(y, x);

        double[] segundo = groundTruth.get(y, x);

        // Leer un "pixel" de la matriz
        if (primer[0] == segundo[0] || (primer[0] != 0 && segundo[0] != 0)) {
          cardIntersection = cardIntersection + 1;
        }
      }
    }
    return (2 * cardIntersection) / (sizeGroundTruth.area() + sizeImagenUmbralizada.area());
  }
  
}