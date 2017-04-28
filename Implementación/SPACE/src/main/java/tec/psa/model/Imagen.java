package tec.psa.model;

import org.opencv.core.Mat;

import tec.psa.segmentacion.Histograma;

public class Imagen {

	private long idImagen;
	private Mat imagen;
	private int tao;
	private String nombreImagen;
	private Histograma histograma;
	private long numeroCelulas;
	private long tiempoProcesamiento;
	
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
