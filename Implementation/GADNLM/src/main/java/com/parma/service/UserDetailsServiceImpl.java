package com.parma.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.parma.configuration.SpringMongoConfiguration;
import com.parma.model.User;
import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private ApplicationContext ctx;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    ctx = new AnnotationConfigApplicationContext(SpringMongoConfiguration.class);
    final MongoOperations mongoOps = (MongoOperations) ctx.getBean("mongoTemplate");
    Query query = new Query(Criteria.where("username").is(username));
    User user = mongoOps.findOne(query, User.class);
    if (user != null) {
      return user;
    } else {
      throw new UsernameNotFoundException("Username not found.");
    }
  }


}
