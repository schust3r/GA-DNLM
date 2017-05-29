package tec.psa.model;

/**
 * Entidad para la clase Lote.
 * 
 * @author Joel Schuster
 *
 */
public class Lote {
  
  private String id;
  private String nombre;
  private long imagenes;
  private String ultimaModificacion;
  private String tiempoProcesamiento;    

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTiempoProcesamiento() {
    return tiempoProcesamiento;
  }

  public void setTiempoProcesamiento(String tiempoProcesamiento) {
    this.tiempoProcesamiento = tiempoProcesamiento;
  }

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
