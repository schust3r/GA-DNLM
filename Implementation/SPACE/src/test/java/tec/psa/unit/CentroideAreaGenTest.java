package tec.psa.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import tec.psa.segmentacion.algoritmos.CentroideAreaGen;
import tec.psa.segmentacion.imagenes.ImageHandler;

public class CentroideAreaGenTest {

  @Test
  public void testObtenerCentroidesAreas() {    
    
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    
    ImageHandler ih = new ImageHandler();
    Mat img = ih.leerImagenGrises("test_files/input/areaCentroideTest.png");
    
    CentroideAreaGen cenarGen = new CentroideAreaGen(); 
    
    String ref = "id,centroideX,centroideY,area\n1,50.0,50.0,7778.0\n";
    
    String resultado = cenarGen.obtenerCentroidesAreas(img);
    
    assertEquals("El resultado no es el esperado", ref, resultado);
    
  }

}
