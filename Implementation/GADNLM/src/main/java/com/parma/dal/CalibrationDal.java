package com.parma.dal;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import com.parma.configuration.SpringMongoConfiguration;
import com.parma.model.Calibration;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CalibrationDal {

  public static Calibration loadCalibration(String title) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // get a single calibration linked to the user
    Query query = new Query(Criteria.where("title").is(title).and("owner").is(auth.getName()));
    return mongoOps.findOne(query, Calibration.class);   
  }

  public static List<Calibration> loadFinishedCalibrations() {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // generate array of calibrations linked to the user
    List<Calibration> cals = new ArrayList<Calibration>();
    Query query = new Query(Criteria.where("owner").is(auth.getName()).and("status").is("DONE"));
    cals = mongoOps.find(query, Calibration.class);
    return cals;
  }

  public static List<Calibration> loadAllCalibrations() {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // generate array of calibrations linked to the user
    List<Calibration> cals = new ArrayList<Calibration>();
    Query query = new Query(Criteria.where("owner").is(auth.getName()));
    cals = mongoOps.find(query, Calibration.class);
    return cals;
  }

  public static void saveCalibration(Calibration cal) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // save the calibration in mongodb
    mongoOps.save(cal, "Calibration");
  }

  public static void removeCalibration(String title) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // search calibration by title
    Query query = new Query(Criteria.where("title").is(title).and("owner").is(auth.getName()));
    // remove the found calibration
    mongoOps.remove(query, Calibration.class);
  }

  public static void updateStatus(String title, double fitness, int curr_gen, String status, String owner ) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // find and update params in the calibration
    //System.out.println(auth.getName());
    Query query = new Query(Criteria.where("title").is(title).and("owner").is(owner));
    Update calUpdate = new Update();
    calUpdate.set("current_fitness", fitness);
    calUpdate.set("current_gen", curr_gen);
    calUpdate.set("status", status);
    mongoOps.updateFirst(query, calUpdate, Calibration.class);
  }

  public static void updateParams(String title, int w, int w_n, int sigma_r, double fitness, String owner) {
	ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    // find and update params in the calibration
    Query query = new Query(Criteria.where("title").is(title).and("owner").is( owner));
    Update calUpdate = new Update();
    calUpdate.set("best_w", w);
    calUpdate.set("best_w_n", w_n);
    calUpdate.set("best_s_r", sigma_r);
    calUpdate.set("best_fitness", fitness);
    // create and get current time for end time
    Date finish = new Date();
    finish.getTime();
    calUpdate.set("endTime", finish);
    // perform the update operation
    mongoOps.updateFirst(query, calUpdate, Calibration.class);
  }


} // end DAL
