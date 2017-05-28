package tec.psa.model;

/**
 * Entidad para la clase Lote.
 * 
 * @author Joel Schuster
 *
 */
public class Lote {
  
  private String nombre;
  private long imagenes;
  private String ultimaModificacion;
  
  public String getNombre() {
    return nombre;
  }
  
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  
  public long getImagenes() {
    return imagenes;
  }
  
  public void setImagenes(long imagenes) {
    this.imagenes = imagenes;
  }
  
  public String getUltimaModificacion() {
    return ultimaModificacion;
  }
  
  public void setUltimaModificacion(String ultimaModificacion) {
    this.ultimaModificacion = ultimaModificacion;
  }

}
