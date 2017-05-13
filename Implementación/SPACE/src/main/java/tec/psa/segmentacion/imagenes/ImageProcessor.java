package tec.psa.segmentacion.imagenes;

import tec.psa.model.Imagen;

import tec.psa.segmentacion.Histograma;

import tec.psa.segmentacion.algoritmos.Etiquetado;

import tec.psa.segmentacion.algoritmos.Kittler;

import tec.psa.segmentacion.algoritmos.Umbralizacion;

import tec.psa.segmentacion.conf.Const;

/**
 * Clase que empaqueta el procesamiento de 
 * una imagen subida al sistema.
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
   * Recibe una imagen y le sobreescribe el resultado de la
   * umbralizacion de inmediato.
   * 
   * @param rutaImg ruta de la imagen guardada en disco
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

}
