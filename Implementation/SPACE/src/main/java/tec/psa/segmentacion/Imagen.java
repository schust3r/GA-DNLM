package tec.psa.segmentacion;

import org.opencv.core.Mat;

public class Imagen {

  private long idImagen;
  private Mat imagen;
  private byte[] imagenBytes;
  private int tao;
  private String nombreImagen;
  private Histograma histograma;
  private long numeroCelulas;
  private long tiempoProcesamiento;
  
  public byte[] getImagenBytes() {
    return imagenBytes;
  }

  public void setImagenBytes(byte[] imagenBytes) {
    this.imagenBytes = imagenBytes;
  }

  public long getIdImagen() {
    return idImagen;
  }

  public void setIdImagen(long idImagen) {
    this.idImagen = idImagen;
  }

  public Mat getImagen() {
    return imagen;
  }

  public void setImagen(Mat imagen) {
    this.imagen = imagen;
  }

  public int getTao() {
    return tao;
  }

  public void setTao(int tao) {
    this.tao = tao;
  }

  public String getNombreImagen() {
    return nombreImagen;
  }

  public void setNombreImagen(String nombreImagen) {
    this.nombreImagen = nombreImagen;
  }

  public Histograma getHistograma() {
    return histograma;
  }

  public void setHistograma(Histograma histograma) {
    this.histograma = histograma;
  }

  public long getNumeroCelulas() {
    return numeroCelulas;
  }

  public void setNumeroCelulas(long numeroCelulas) {
    this.numeroCelulas = numeroCelulas;
  }

  public long getTiempoProcesamiento() {
    return tiempoProcesamiento;
  }

  public void setTiempoProcesamiento(long tiempoProcesamiento) {
    this.tiempoProcesamiento = tiempoProcesamiento;
  }

}
