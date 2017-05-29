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

      Mat imagen = Imgcodecs.imdecode(new MatOfByte(bytes),
          Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
      
      // StringBuilder para formar el csv
      String csv = "id,centroideX,centroideY,area\n";

      if (imagen == null) {
        throw new NullPointerException("Imagen de entrada no encontrada.");
      }

      ArrayList<MatOfPoint> contornos = new ArrayList<>();
      Mat hierarchy = new Mat();
      Imgproc.findContours(imagen, contornos, hierarchy, 
          Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

      int index = 1;
      // Recorre los puntos del contorno y los dibuja
      for (MatOfPoint matOfPoint : contornos) {        
        
        // calcular el area
        double area = Imgproc.contourArea(matOfPoint);
        
        // Definir el centroide
        Point centroid = new Point();
        
        if (area > 0) {
          Moments moments = Imgproc.moments(matOfPoint);  
          centroid.x = moments.get_m10() / moments.get_m00();    
          centroid.y = moments.get_m01() / moments.get_m00();
        } else {
          area = 1;
          centroid.x = matOfPoint.get(0, 0)[0];
          centroid.y = matOfPoint.get(0, 0)[1];
        }
        // agregar valores al String
        csv += index + "," + centroid.x + "," + centroid.y + "," + area + "\n";               
        index++;
      }           

      return csv;

    } catch (NullPointerException ex) {
      ex.printStackTrace();
      return "";
    } catch (Exception ex) {
      ex.printStackTrace();
      return "";
    }
  }

}
