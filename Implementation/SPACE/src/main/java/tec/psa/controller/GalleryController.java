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
public class GalleryController {

  /**
   * Cargar los lotes a partir de las imágenes en base de datos.
   * 
   * @param servletRequest
   *          solicitud del servlet
   * @param model
   *          el modelo de Spring
   */
  @RequestMapping(value = "/gallery", params = { "lote" , "i" }, method = RequestMethod.GET)
  public String cargarLotes(
      @RequestParam(value = "lote") String nombreLote,
      @RequestParam(value = "i") int ival, 
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
          .and("metadata.lote").is(nombreLote))
          .with(new Sort(new Order(Direction.DESC, "uploadDate")));
      
      List<GridFSDBFile> files = gridOperations.find(query);     
      
      model.addAttribute("indice", ival);
      model.addAttribute("maxIndice", (int)Math.ceil((double)files.size() / 8.0)); 
      model.addAttribute("nombreLote", nombreLote);

      List<ImagenGaleria> listaImagenes1 = new ArrayList<>();
      List<ImagenGaleria> listaImagenes2 = new ArrayList<>();      

      int i = (ival - 1) * 8;
      int midPoint = i + 4;
      int stop = Math.min(i + 8, files.size());
      
      while (i < stop) {
        // Extraer archivo de la lista
        GridFSDBFile file = files.get(i);
        
        // Objeto ImagenGaleria
        ImagenGaleria nuevaImagen = new ImagenGaleria();                
        
        // El identificador unico de la imagen
        nuevaImagen.setId(file.getId().toString());
        
        // Obtener y asignar el byte array de la imagen en Base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        file.writeTo(baos);
        nuevaImagen.setImgBase64(new String(Base64.encode(baos.toByteArray())));   
        
        // Agregar al arreglo de imagenes
        if (i < midPoint) {
          listaImagenes1.add(nuevaImagen);
        } else {
          listaImagenes2.add(nuevaImagen);
        }
        
        i++; // aumentar el indice        
      }

      model.addAttribute("listaImgs1", listaImagenes1);
      model.addAttribute("listaImgs2", listaImagenes2);

      return "gallery";

    } catch (Exception ex) {
      ex.printStackTrace();
      return "home";
    }
  }

}