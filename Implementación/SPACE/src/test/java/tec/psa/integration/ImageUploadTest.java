package tec.psa.integration;

import static org.junit.Assert.assertTrue;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import tec.psa.configuration.SpringMongoConfiguration;

public class ImageUploadTest {

  @Test
  public void uploadImageTest() throws IOException {
    
    // Cargar la imagen
    BufferedImage image = ImageIO.read(new File("test_files/input/cell.png"));
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ImageIO.write(image, "png", os);
    InputStream is = new ByteArrayInputStream(os.toByteArray());
    
    ApplicationContext ctx = 
        new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    final GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");
    
    // guardar el archivo en la base de datos Mongo
    GridFSFile res = gridOperations.store(is, "imagenDePrueba_DVSewg2gqa", "image/png");
    
    assertTrue("Hubo un error al subir la imagen.", res != null);
    
  }

}
