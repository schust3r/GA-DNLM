package tec.psa.controller;

import com.mongodb.gridfs.GridFSDBFile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tec.psa.configuration.SpringMongoConfiguration;
import tec.psa.model.ImagenGaleria;

@Controller
public class DetalleController {

  /**
   * Cargar los detalles de una imagen desde la base de datos.
   * 
   * @param idImagen el identificador de la imagen
   * @param servletRequest solicitud del Servlet
   * @param model el modelo de Spring
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

}