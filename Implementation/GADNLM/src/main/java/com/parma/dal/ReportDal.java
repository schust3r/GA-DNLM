package com.parma.dal;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.parma.configuration.SpringMongoConfiguration;
import com.parma.model.FitnessReport;
import com.parma.model.TimeReport;

public class ReportDal {
  
  public static TimeReport saveTimeReport() {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // get a single calibration linked to the user
    Query query = new Query(Criteria.where("owner").is(auth.getName()));
    return mongoOps.findOne(query, TimeReport.class);
  }
  
  public static void removeReports(String calibration) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // remove all reports associated with the calibration and user 
    Query query = new Query(Criteria.where("calibration").is(calibration).and("owner").is(auth.getName()));
    mongoOps.remove(query, FitnessReport.class);
    mongoOps.remove(query, TimeReport.class);
  }

}
