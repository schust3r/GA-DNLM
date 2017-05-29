package tec.psa.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import tec.psa.configuration.SpringMongoConfiguration;

import tec.psa.model.UploadedFile;
import tec.psa.model.User;
import tec.psa.segmentacion.Imagen;
import tec.psa.segmentacion.imagenes.ImageProcessor;

@Controller
public class UploadController {

  // Las extensiones permitidas
  private String[] allowedExts = { "bmp", "tif", "png", "jpg" };
  
  /**
   * Atender método GET para subir un lote de imágenes.
   * 
   * @param userForm formulario de datos del usuario
   * @param model modelo de Spring
   * @return ruta GET del upload
   */
  @RequestMapping(value = "/upload", method = RequestMethod.GET)
  public String upload(@ModelAttribute("user") User userForm, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("usuario", auth.getName());
    return "upload";
  }

  /**
   * Iniciar guardado de archivo en base de datos.
   * 
   * @param servletRequest
   *          petición del Servlet
   * @param uploadedFile
   *          archivo que se está subiendo
   * @param nombreLote
   *          nombre del lote
   * @param bindingResult
   *          resultado de la acción
   * @param model
   *          modelo de Spring
   */
  @RequestMapping(value = "/upload", method = RequestMethod.POST)
  public void saveFile(HttpServletRequest servletRequest, @ModelAttribute UploadedFile uploadedFile,
      @RequestParam("loteNombre") String nombreLote, BindingResult bindingResult, Model model) {

    MultipartFile multipartFile = uploadedFile.getMultipartFile();
    String fileName = multipartFile.getOriginalFilename();

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    String ext = FilenameUtils.getExtension(fileName);

    try {
      // verificar que el tipo de archivo sea válido
      if (Arrays.asList(allowedExts).contains(ext)) {
        
        // obtener input stream del archivo subido
        byte[] inputStream = multipartFile.getBytes();
        
        // comenzar procesamiento de la imagen - de momento FIFO
        procesarImagen(inputStream, auth.getName(), nombreLote, fileName, ext);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Procesamiento asíncrono de la imagen subida.
   * 
   * @param imagenInput
   *          bytes de entrada
   * @param nombreUsuario
   *          nombre del usuario
   * @param nombreLote
   *          nombre del lote
   * @param fileName
   *          nombre plano del archivo
   * @param ext
   *          extensión del archivo
   */
  @Async
  private void procesarImagen(byte[] imagenInput, String nombreUsuario, 
      String nombreLote, String fileName,
      String ext) {

    try {

      ApplicationContext ctx = 
          new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
      final GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

      // Llamar al procesador de imagen y pasarle los bytes
      ImageProcessor ip = new ImageProcessor();
      Imagen imagen = ip.procesarImagen(imagenInput);

      DBObject metaData = new BasicDBObject();
      metaData.put("usuario", nombreUsuario);
      metaData.put("lote", nombreLote);
      metaData.put("conteo", imagen.getNumeroCelulas());
      metaData.put("tiempo_procesamiento", imagen.getTiempoProcesamiento());
      metaData.put("umbral", imagen.getTao());
      
      // Extraer Stream desde MatOfBytes
      InputStream inputStreamData = new ByteArrayInputStream(imagen.getImagenBytes());   
      
      // guardar el archivo en la base de datos Mongo
      gridOperations.store(inputStreamData, fileName, "image/" + ext, metaData);
      
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}