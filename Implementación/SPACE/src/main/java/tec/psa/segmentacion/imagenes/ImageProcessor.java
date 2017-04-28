package tec.psa.segmentacion.imagenes;

import tec.psa.model.Imagen;
import tec.psa.segmentacion.Histograma;
import tec.psa.segmentacion.algoritmos.Etiquetado;
import tec.psa.segmentacion.algoritmos.Kittler;
import tec.psa.segmentacion.algoritmos.Umbralizacion;
import tec.psa.segmentacion.conf.Const;

/**
 * 
 * @author Joel Schuster
 *
 */
public class ImageProcessor {
	
	// Manejo de imágenes
	private ImageHandler ih;
	
	// Algoritmo de Kittler
	private Kittler k;
	
	// Algoritmo de umbralización OpenCV
	private Umbralizacion umb;
	
	// Algoritmo de etiquetado basado en OpenCV
	private Etiquetado etq;

	/**
	 * Constructor
	 */
	public ImageProcessor() {
		this.k = new Kittler();
		this.ih = new ImageHandler();
		this.umb = new Umbralizacion();
		this.etq = new Etiquetado();
	}
	
	/**
	 * Recibe una imagen y le sobreescribe el resultado de la
	 * umbralizacion de inmediato
	 * 
	 * @param nombreImg	nombre de la imagen guardada en disco
	 */
	public Imagen procesarImagen(String rutaImg) {
	
		// Contar tiempo
		long startTime = System.nanoTime();   
		
		// Crea una nueva imagen y la asigna
		Imagen img = new Imagen();
		img.setImagen(ih.leerImagenGrises(rutaImg));			
		img.setHistograma(new Histograma(img.getImagen(), Const.LIMITE));
		k.setHistograma(img.getHistograma());
		k.calcularUmbral();
		img.setTao(k.getTao());
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
