package tec.psa.model;

/**
 * Entidad para la Imagen en GalleryController y gallery.html
 * 
 * @author Joel Schuster
 *
 */
public class ImagenGaleria {
  
  private String id;
  private String nombreArchivo;
  private String nombreLote;
  private String imgBase64;
  private String indice;  
  private String tiempoProcesamiento;
  private String numeroCelulas;   
  private String imageSize;
  private String umbral;
  
  public String getNombreLote() {
    return nombreLote;
  }

  public void setNombreLote(String nombreLote) {
    this.nombreLote = nombreLote;
  }  
  
  public String getUmbral() {
    return umbral;
  }

  public void setUmbral(String umbral) {
    this.umbral = umbral;
  }

  public String getImageSize() {
    return imageSize;
  }

  public void setImageSize(String imageSize) {
    this.imageSize = imageSize;
  }

  public String getNombreArchivo() {
    return nombreArchivo;
  }

  public void setNombreArchivo(String nombreArchivo) {
    this.nombreArchivo = nombreArchivo;
  }

  public String getTiempoProcesamiento() {
    return tiempoProcesamiento;
  }

  public void setTiempoProcesamiento(String tiempoProcesamiento) {
    this.tiempoProcesamiento = tiempoProcesamiento;
  }

  public String getNumeroCelulas() {
    return numeroCelulas;
  }

  public void setNumeroCelulas(String numeroCelulas) {
    this.numeroCelulas = numeroCelulas;
  }  
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getIndice() {
    return indice;
  }
  
  public void setIndice(String indice) {
    this.indice = indice;
  }
  
  public String getImgBase64() {
    return imgBase64;
  }
  
  public void setImgBase64(String imgBase64) {
    this.imgBase64 = imgBase64;
  }  

}
