package tec.psa.controller;

import com.mongodb.gridfs.GridFSDBFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tec.psa.configuration.SpringMongoConfiguration;
import tec.psa.model.Lote;

@Controller
public class LoteController {

  /**
   * Borrar un lote completo de la base de datos.
   * 
   * @param nombreLote el nombre del lote a borrar
   * @param servletRequest la solicitud del server 
   * @param model el modelo de Spring
   * @return ruta lotes
   */
  @RequestMapping(value = "/eliminarLote", params = { "lote" }, method = RequestMethod.GET)
  public String eliminarLote(@RequestParam(value = "lote") String nombreLote, 
      HttpServletRequest servletRequest, Model model) {

    // Autenticacion para obtener nombre de usuario
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String nombreUsuario = auth.getName();
    model.addAttribute("usuario", nombreUsuario);

    try {

      ApplicationContext ctx = 
          new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
      final GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

      Query query = new Query(Criteria.where("metadata.usuario").is(nombreUsuario)
          .and("metadata.lote").is(nombreLote));

      // operacion de borrado del lote
      gridOperations.delete(query);

      return "redirect:/lotes";

    } catch (Exception ex) {
      ex.printStackTrace();
      return "home";
    }
  }  

  /**
   * Cargar los lotes a partir de las imágenes en base de datos.
   * 
   * @param servletRequest
   *          solicitud del servlet
   * @param model
   *          el modelo de Spring
   */
  @RequestMapping(value = "/lotes", method = RequestMethod.GET)
  public String cargarLotes(HttpServletRequest servletRequest, Model model) {

    // Autenticacion para obtener nombre de usuario
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String nombreUsuario = auth.getName();
    model.addAttribute("usuario", nombreUsuario);

    try {

      ApplicationContext ctx = 
          new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
      final GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

      Query query = new Query(Criteria.where("metadata.usuario").is(nombreUsuario));
      List<GridFSDBFile> files = gridOperations.find(query);

      Set<String> lotesUnicos = new HashSet<>();

      for (GridFSDBFile file : files) {
        lotesUnicos.add(file.getMetaData().get("lote").toString());
      }

      List<Lote> listaLotes = new ArrayList<>();

      for (String nombreLote : lotesUnicos) {
        // Objeto Lote
        Lote lote = new Lote();

        // Indicar el nombre del lote
        lote.setNombre(nombreLote);

        // Para calcular la cantidad de imagenes en el lote
        query = new Query(Criteria.where("metadata.usuario").is(nombreUsuario)
            .and("metadata.lote").is(nombreLote));
        List<GridFSDBFile> imgs = gridOperations.find(query);
        lote.setImagenes(imgs.size());
        
        // Calcular la duración al procesar el lote
        long tiempoProcesamiento = 0;
        for (GridFSDBFile img : imgs) {
          String t = img.getMetaData().get("tiempo_procesamiento").toString();
          tiempoProcesamiento += Long.parseLong(t);
        }        
        // guardar tiempo como String en segundos
        double tiempoTotal = (double)tiempoProcesamiento / 1000000000.0;
        lote.setTiempoProcesamiento(String.valueOf(Math.round(tiempoTotal * 1000.0) / 1000.0) 
            + " segundos"); 

        // Obtener la fecha del ultimo archivo modificado
        query = new Query(Criteria.where("metadata.usuario").is(nombreUsuario)
            .and("metadata.lote").is(nombreLote))
            .with(new Sort(new Order(Direction.DESC, "uploadDate"))).limit(1);
        imgs = gridOperations.find(query);
        lote.setUltimaModificacion(imgs.get(0).getUploadDate().toString());

        // Agregar lote a la lista para la tabla
        listaLotes.add(lote);
      }

      model.addAttribute("listaLotes", listaLotes);

      return "lotes";

    } catch (Exception ex) {
      ex.printStackTrace();
      return "home";
    }
  }

}