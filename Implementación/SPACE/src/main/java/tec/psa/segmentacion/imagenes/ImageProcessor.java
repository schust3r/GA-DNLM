package tec.psa.segmentacion.imagenes;

import org.opencv.core.Mat;

import tec.psa.segmentacion.Histograma;
import tec.psa.segmentacion.algoritmos.Etiquetado;
import tec.psa.segmentacion.algoritmos.Kittler;
import tec.psa.segmentacion.algoritmos.Umbralizacion;
import tec.psa.segmentacion.conf.Const;

/**
 * 
 * @author Joel
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
	public void procesarImagen(String nombreImg) {	
		Mat imagen = ih.leerImagenGrises(Const.IMG_DIR + nombreImg);
		Histograma hist = new Histograma(imagen, Const.LIMITE);
		k.setHistograma(hist);		
		k.calcularUmbral();
		umb.aplicarUmbral(imagen, k.getTao());
		//imagen = etq.etiquetarCelulas(imagen);
		ih.guardarImagen(Const.IMG_DIR, "salida", "bmp", imagen);
		
	}
	
}
