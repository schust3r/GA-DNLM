package tec.psa.integration;

import static org.junit.Assert.assertTrue;

import com.mongodb.gridfs.GridFSDBFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import tec.psa.configuration.SpringMongoConfiguration;

public class ImageDownloadTest {

  /**
   * Esta prueba depende de ImageUploadTest.java, la cual debe
   * correrse primero antes de ejecutar esta. De lo contrario
   * ImageDownloadTest no se cumple.
   * 
   * @throws IOException por si se cae
   */
  @Test
  public void downloadImageTest() throws IOException {
    
    // Conexion a base de datos Mongo
    ApplicationContext ctx = 
        new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    final GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

    Query query = new Query(Criteria.where("filename").is("imagenDePrueba_DVSewg2gqa"));        
    
    List<GridFSDBFile> files = gridOperations.find(query);     
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    files.get(0).writeTo(baos);
    byte[] bytes = baos.toByteArray();
    
    // Escribir el archivo en disco desde la BD
    File targetFile = new File("test_files/output/cellDesdeBD.png");
    OutputStream outStream = new FileOutputStream(targetFile);
    outStream.write(bytes);
    outStream.close();
    
    assertTrue("El archivo no existe en disco o está corrupto.", 
        targetFile.length() > 400000 && targetFile.length() < 500000);
    
    // Borrar la img de la base de datos
    gridOperations.delete(query);
        
  }

}
