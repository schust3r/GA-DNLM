package com.parma.dal;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.parma.configuration.SpringMongoConfiguration;
import com.parma.model.Image;

public class ImageDal {
  
  public static Image loadImage(String id) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // get a single calibration linked to the user
    Query query = new Query(Criteria.where("_id").is(id).and("owner").is(auth.getName()));
    return mongoOps.findOne(query, Image.class);
  }
  
  public static void saveImage(Image img) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // save image to the database    
    mongoOps.save(img, "Image");
  }
  
}
