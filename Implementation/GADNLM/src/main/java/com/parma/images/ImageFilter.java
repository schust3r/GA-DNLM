package com.parma.images;

import com.parma.segmentation.Otsu;
import com.parma.segmentation.Thresholding;

/**
 * Clase que empaqueta el procesamiento de una imagen subida al sistema.
 * 
 * @author Joel Schuster
 *
 */
public class ImageFilter {

  // Manejo de imágenes
  private ImageHandler ih;

  // Algoritmo de kittlerittler
  private Otsu otsu;

  // Algoritmo de umbralización OpenCV
  private Thresholding umb;

  /**
   * Constructor.
   */
  public ImageFilter() {
    this.otsu = new Otsu();
    this.ih = new ImageHandler();
    this.umb = new Thresholding();
  }
  
  


}
