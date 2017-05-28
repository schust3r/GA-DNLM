package tec.psa.segmentacion.algoritmos;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import tec.psa.segmentacion.conf.Const;
import tec.psa.segmentacion.imagenes.ImageHandler;

public class CentroideAreaGen {

  /**
   * Cálculo de áreas y centroides en formato CSV para escribir en archivo.
   * 
   * @param bytes
   *          imagen en bytes para convertir a Mat
   * @return string con areas y centroides
   */
  public String obtenerCentroidesAreas(byte[] bytes) {

    try {

      // llamar librería nativa
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

      Mat imagen = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
      
      // StringBuilder para formar el csv
      StringBuilder csv = new StringBuilder();
      csv.append("id,centroideX,centroideY,area\n");

      if (imagen == null) {
        throw new NullPointerException("Imagen de entrada no encontrada.");
      }

      ArrayList<MatOfPoint> contornos = new ArrayList<MatOfPoint>();
      Mat hierarchy = new Mat();
      Imgproc.findContours(imagen, contornos, hierarchy, 
          Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

      int index = 1;
      // Recorre los puntos del contorno y los dibuja
      for (MatOfPoint matOfPoint : contornos) {               

        csv.append(index);
        csv.append(",");

        // Calcular y guardar los centroides
        Moments moments = Imgproc.moments(matOfPoint);
        
        Point centroid = new Point();

        // centroide en X
        centroid.x = moments.get_m10() / moments.get_m00();
        csv.append(centroid.x);
        csv.append(",");

        // centroide en Y
        centroid.y = moments.get_m01() / moments.get_m00();
        csv.append(centroid.y);
        csv.append(",");

        // calcular el area
        double area = Imgproc.contourArea(matOfPoint);
        csv.append(area);
        csv.append("\n");

        // aumentar el contador en 1
        index++;

      }

      return csv.toString();

    } catch (NullPointerException ex) {
      ex.printStackTrace();
      return "";
    } catch (Exception ex) {
      ex.printStackTrace();
      return "";
    }
  }

}
