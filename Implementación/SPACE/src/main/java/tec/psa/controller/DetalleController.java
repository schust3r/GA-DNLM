package tec.psa.controller;

import com.mongodb.gridfs.GridFSDBFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tec.psa.configuration.SpringMongoConfiguration;
import tec.psa.model.ImagenGaleria;
import tec.psa.model.UploadedFile;
import tec.psa.segmentacion.algoritmos.CentroideAreaGen;
import tec.psa.segmentacion.algoritmos.Dice;

@Controller
public class DetalleController {

  /**
   * Cargar los detalles de una imagen desde la base de datos.
   * 
   * @param idImagen
   *          el identificador de la imagen
   * @param servletRequest
   *          solicitud del Servlet
   * @param model
   *          el modelo de Spring
   * @return pagina con detalles de la imagen
   */
  @RequestMapping(value = "/detalle", params = { "id", "i" }, method = RequestMethod.GET)
  public String cargarDetalles(@RequestParam(value = "id") String idImagen,
      @RequestParam(value = "i", required = false) int ival, 
      HttpServletRequest servletRequest, Model model) {

    // Autenticacion para obtener nombre de usuario
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String nombreUsuario = auth.getName();
    model.addAttribute("usuario", nombreUsuario);
    model.addAttribute("ival", ival);
    model.addAttribute("id", idImagen);

    try {

      ApplicationContext ctx = 
          new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
      final GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

      Query query = new Query(Criteria.where("metadata.usuario").is(nombreUsuario)
          .and("_id").is(idImagen));

      // Seleccionar la unica imagen
      GridFSDBFile file = gridOperations.find(query).get(0);

      ImagenGaleria imagenDetalle = new ImagenGaleria();

      // settear id de la imagen
      imagenDetalle.setId(idImagen);

      // settear el nombre del archivo
      imagenDetalle.setNombreArchivo(file.getFilename().toString());

      // settear el nombre del lote del archivo
      imagenDetalle.setNombreLote(file.getMetaData().get("lote").toString());

      // settear el total de celulas encontradas
      imagenDetalle.setNumeroCelulas(file.getMetaData().get("conteo").toString());

      // settear el umbral encontrado
      imagenDetalle.setUmbral(file.getMetaData().get("umbral").toString());

      // settera el tiempo total de procesamiento
      imagenDetalle.setTiempoProcesamiento(file.getMetaData()
          .get("tiempo_procesamiento").toString());

      // Obtener y asignar el byte array de la imagen en Base64
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      file.writeTo(baos);
      imagenDetalle.setImgBase64(new String(Base64.encode(baos.toByteArray())));

      model.addAttribute("imagen", imagenDetalle);
      model.addAttribute("lote", imagenDetalle.getNombreLote());

      return "detalle";

    } catch (Exception ex) {
      ex.printStackTrace();
      return "home";
    }
  }

  /**
   * Eliminar una imagen de un lote.
   * 
   * @param idImagen
   *          el identificador de la imagen
   * @param servletRequest
   *          la solicitud del servlet
   * @param model
   *          el modelo de Spring
   * @return la visualización del lote
   */
  @RequestMapping(value = "/eliminarImagen", params = { "id" }, method = RequestMethod.GET)
  public String eliminarImagen(@RequestParam(value = "id") String idImagen, 
      HttpServletRequest servletRequest, Model model) {

    // Autenticacion para obtener nombre de usuario
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String nombreUsuario = auth.getName();
    model.addAttribute("usuario", nombreUsuario);

    try {

      ApplicationContext ctx = 
          new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
      final GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

      Query query = new Query(Criteria.where("_id").is(idImagen));

      // Seleccionar la unica imagen
      GridFSDBFile file = gridOperations.find(query).get(0);
      String nombreLote = file.getMetaData().get("lote").toString();

      // operacion de borrado de la imagen
      gridOperations.delete(query);

      // Indicar con el flag que se ha eliminado la imagen.
      model.addAttribute("eliminado", "Se ha eliminado la imagen del lote");

      // Comprobar si quedan imagenes en ese lote
      Query imagenesLote = new Query(
          Criteria.where("metadata.lote").is(nombreLote).and("metadata.usuario").is(nombreUsuario));
      long restantes = gridOperations.find(imagenesLote).size();

      if (restantes == 0) {
        // redirigir a lotes
        return "redirect:/lotes";
      } else {
        // redirigir a esa galeria
        return "redirect:/gallery?lote=" + nombreLote + "&i=1";
      }

    } catch (Exception ex) {
      ex.printStackTrace();
      return "lotes";
    }
  }

  /**
   * Generar y devolver un informe descargable.
   * 
   * @param idImagen
   *          el identificador de la imagen
   * @param servletRequest
   *          la solicitud del servlet
   * @param model
   *          el modelo de Spring
   */
  @Async
  @RequestMapping(value = "/generarInforme", params = { "id" }, method = RequestMethod.GET)
  public void generarInforme(@RequestParam(value = "id") String idImagen, Model model, 
      HttpServletRequest servletRequest, HttpServletResponse response) {

    // Autenticacion para obtener nombre de usuario
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String nombreUsuario = auth.getName();
    model.addAttribute("usuario", nombreUsuario);

    // Decirle al navegador que va a devolver un archivo
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment;filename=informe.csv");

    try {

      ApplicationContext ctx = 
          new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
      final GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

      Query query = new Query(Criteria.where("_id").is(idImagen));

      // Seleccionar la unica imagen
      GridFSDBFile file = gridOperations.find(query).get(0);

      // Convertir archivo a byte array
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      file.writeTo(baos);
      byte[] imgSegAuto = baos.toByteArray();

      CentroideAreaGen cenarGen = new CentroideAreaGen();
      String csv = cenarGen.obtenerCentroidesAreas(imgSegAuto);

      response.getOutputStream().print(csv);
      response.getOutputStream().flush();
            
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  /**
   * Función para evaluar ground truth respecto a la imagen indicada.
   * 
   * @param idImagen el identificador de la imagen
   * @param uploadedFile el archivo de groundtruth a subir
   * @param model el modelo de Spring
   * @param servletRequest la solicitud el Servlet
   */
  @ResponseBody
  @RequestMapping(value = "/evaluarGroundtruth", method = RequestMethod.POST)
  public String evaluarGroundtruth(
      @RequestParam(value = "id") String idImagen,
      @RequestParam(value = "ival") String ival, 
      @ModelAttribute UploadedFile uploadedFile, 
      Model model, 
      HttpServletRequest servletRequest) {
    
    // Archivo multipart de subida (groundtruth)
    MultipartFile multipartFile = uploadedFile.getMultipartFile();

    // Autenticacion para obtener nombre de usuario
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String nombreUsuario = auth.getName();
    
    model.addAttribute("usuario", nombreUsuario);    
    
    try {

      ApplicationContext ctx = 
          new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
      final GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

      Query query = new Query(Criteria.where("_id").is(idImagen));

      // Seleccionar la unica imagen
      GridFSDBFile file = gridOperations.find(query).get(0);

      // Convertir archivo a byte array
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      file.writeTo(baos);
      byte[] imgSegAuto = baos.toByteArray();
      
      double diceResult = Dice.calcularDice(imgSegAuto, multipartFile.getBytes());
      
      return "Se ha encontrado un " 
          + (100.0 * Math.round(diceResult * 1000.0) / 1000.0) + "% de similitud.";
            
    } catch (Exception ex) {
      ex.printStackTrace();
      return "Error.";
    }
  }  

}