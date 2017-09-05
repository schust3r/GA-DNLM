package tec.psa.unit;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.opencv.core.Mat;

import tec.psa.segmentacion.algoritmos.Umbralizacion;
import tec.psa.segmentacion.imagenes.ImageHandler;

public class UmbralizacionTest {

  @Test
  public void testAplicarUmbral() {

    ImageHandler ih = new ImageHandler();
    Mat img = ih.leerImagenGrises("test_files/input/ojo_original.png");

    Mat imgRef = ih.leerImagenGrises("test_files/input/ojo_umbral_150.png");

    Umbralizacion umb = new Umbralizacion();
    umb.aplicarUmbral(img, 150 - 1); // Empieza desde índice cero

    int[][] data1 = new int[img.rows()][img.cols()];
    int[][] data2 = new int[imgRef.rows()][imgRef.cols()];

    // Cargar información importante de píxeles en arreglo
    for (int x = 0; x < img.rows(); x++) {
      for (int y = 0; y < img.cols(); y++) {
        data1[x][y] = (int) img.get(x, y)[0];
        data2[x][y] = (int) imgRef.get(x, y)[0];
      }
    }

    // Verificar si el resultado es el mismo
    assertArrayEquals("Imágenes no dieron mismo resultado", data1, data2);
  }

}
