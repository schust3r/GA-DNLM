package tec.psa.segmentacion.imagenes;

import org.opencv.core.Core;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import tec.psa.segmentacion.Histograma;
import tec.psa.segmentacion.Imagen;
import tec.psa.segmentacion.algoritmos.Etiquetado;

import tec.psa.segmentacion.algoritmos.Kittler;

import tec.psa.segmentacion.algoritmos.Umbralizacion;

import tec.psa.segmentacion.conf.Const;

/**
 * Clase que empaqueta el procesamiento de una imagen subida al sistema.
 * 
 * @author Joel Schuster
 *
 */
public class ImageProcessor {

  // Manejo de imágenes
  private ImageHandler ih;

  // Algoritmo de kittlerittler
  private Kittler kittler;

  // Algoritmo de umbralización OpenCV
  private Umbralizacion umb;

  // Algoritmo de etiquetado basado en OpenCV
  private Etiquetado etq;

  /**
   * Constructor.
   */
  public ImageProcessor() {
    this.kittler = new Kittler();
    this.ih = new ImageHandler();
    this.umb = new Umbralizacion();
    this.etq = new Etiquetado();
  }

  /**
   * Recibe una imagen y le sobreescribe el resultado de la umbralizacion de
   * inmediato.
   * 
   * @param rutaImg
   *          ruta de la imagen guardada en disco
   */
  public Imagen procesarImagen(String rutaImg) {       

    // Contar tiempo
    final long startTime = System.nanoTime();

    // Crea una nueva imagen y la asigna
    Imagen img = new Imagen();
    img.setImagen(ih.leerImagenGrises(rutaImg));
    img.setHistograma(new Histograma(img.getImagen(), Const.LIMITE));
    kittler.setHistograma(img.getHistograma());
    kittler.calcularUmbral();
    img.setTao(kittler.getTao());
    umb.aplicarUmbral(img.getImagen(), img.getTao());
    img.setNumeroCelulas(etq.getConteoCelulas(img.getImagen()));
    img.setImagen(etq.etiquetarCelulas(img.getImagen()));
    ih.sobreescribirImagen(rutaImg, img.getImagen());

    // Obtener estimacion del tiempo en nanosegundos
    long estimatedTime = System.nanoTime() - startTime;

    img.setTiempoProcesamiento(estimatedTime);

    return img;
  }

  /**
   * Recibe una imagen y le sobreescribe el resultado de la umbralizacion de
   * inmediato.
   * 
   * @param byteImagen
   *          arreglo de bytes de la imagen
   */
  public Imagen procesarImagen(byte[] byteImagen) {

    try {
      // llamar librería nativa
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      
      // CONTADOR: empezar a contar el tiempo
      final long startTime = System.nanoTime();

      // Crea una nueva imagen y la asigna
      Imagen img = new Imagen();
      
      // Crear una Mat de OpenCV con el arreglo de bytes
      MatOfByte matrizBytes = new MatOfByte(byteImagen);
      
      // Settear la imagen Mat a partir de la MatOfBytes
      img.setImagen(Imgcodecs.imdecode(matrizBytes, 
          Imgcodecs.IMREAD_GRAYSCALE));
      
      // Calcular el histograma de la imagen
      img.setHistograma(new Histograma(img.getImagen(), Const.LIMITE));
      
      // Settear el Histograma en Kittler y calcular umbral
      kittler.setHistograma(img.getHistograma());
      kittler.calcularUmbral();
      
      // Settear el umbral de binarizacion de la imagen
      img.setTao(kittler.getTao());
      umb.aplicarUmbral(img.getImagen(), img.getTao()); // aplicar el umbral
      
      // Indicar el número de células de la imagen
      img.setNumeroCelulas(etq.getConteoCelulas(img.getImagen()));
      
      // Aplicar la coloración de la imagen
      img.setImagen(etq.etiquetarCelulas(img.getImagen()));
      
      // Settear el arreglo de bytes para la imagen procesada
      Imgcodecs.imencode(".bmp", img.getImagen(), matrizBytes);
      img.setImagenBytes(matrizBytes.toArray());

      // CONTADOR: Obtener estimacion del tiempo en nanosegundos
      long estimatedTime = System.nanoTime() - startTime;

      img.setTiempoProcesamiento(estimatedTime);

      return img;

    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }


}
