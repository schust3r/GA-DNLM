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
import com.parma.model.FitnessReport;
import com.parma.model.TimeReport;

public class ReportDal {


  public static void removeReports(String calibration) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // remove all reports associated with the calibration and user
    Query query =
        new Query(Criteria.where("calibration").is(calibration).and("owner").is(auth.getName()));
    mongoOps.remove(query, FitnessReport.class);
    mongoOps.remove(query, TimeReport.class);
  }


  public static void saveFitnessReport(FitnessReport fitnessReport) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    mongoOps.save(fitnessReport, "FitnessReport");
  }


  public static void saveTimeReport(TimeReport timeReport) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    mongoOps.save(timeReport, "TimeReport");
  }


  public static List<FitnessReport> loadFitnessReports(String calibration) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    List<FitnessReport> reports = new ArrayList<>();
    Query query =
        new Query(Criteria.where("calibration").is(calibration).and("owner").is(auth.getName()));
    reports = mongoOps.find(query, FitnessReport.class);
    return reports;
  }


  public static List<TimeReport> loadTimeReports(String calibration, String type) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    List<TimeReport> reports = new ArrayList<>();
    Query query =
        new Query(Criteria.where("calibration").is(calibration)
            .and("owner").is(auth.getName())
            .and("type").is(type));
    reports = mongoOps.find(query, TimeReport.class);
    return reports;
  }

}
