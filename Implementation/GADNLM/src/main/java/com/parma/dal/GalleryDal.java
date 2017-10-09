package com.parma.dal;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.parma.configuration.SpringMongoConfiguration;
import com.parma.model.Image;

public class GalleryDal {

  private static Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  private static ApplicationContext ctx =
      new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);

  public static List<Image> loadGalleries() {
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // get all images linked to the user
    List<Image> galleries = new ArrayList<Image>();
    Query query = new Query(Criteria.where("owner").is(auth.getName()));
    galleries = mongoOps.find(query, Image.class);
    return galleries;
  }

  public static List<Image> loadGallery(String group) {
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // get a single gallery linked to the user
    List<Image> images = new ArrayList<Image>();
    Query query = new Query(Criteria.where("owner").is(auth.getName()).and("group").is(group));
    images = mongoOps.find(query, Image.class);
    return images;
  }
  
  public static void removeGallery(String group) {
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // get a single calibration linked to the user    
    Query query = new Query(Criteria.where("owner").is(auth.getName()).and("group").is(group));    
    mongoOps.remove(query, Image.class);
  }

  
}
