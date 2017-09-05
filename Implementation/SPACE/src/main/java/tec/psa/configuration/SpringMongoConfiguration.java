package tec.psa.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * Spring MongoDB configuration file.
 *
 * @author Joel Schuster
 */
@Configuration
public class SpringMongoConfiguration extends AbstractMongoConfiguration {

  @Bean
  public GridFsTemplate gridFsTemplate() throws Exception {
    return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
  }

  @Override
  protected String getDatabaseName() {
    return "space";
  }

  @Override
  @Bean
  public Mongo mongo() throws Exception {
    return new MongoClient("127.0.0.1", 27017);
  }

}