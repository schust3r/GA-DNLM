package com.parma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import java.lang.reflect.Field;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class GaDnlmApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    try {
      // load opencv dll path from properties
      setLibraryPath("C:\\opencv33\\opencv\\build\\java\\x64");  
      // run spring boot app
      SpringApplication.run(GaDnlmApplication.class, args);
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    }
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(GaDnlmApplication.class);
  }

  public static void setLibraryPath(String path) throws NoSuchFieldException, SecurityException,
      IllegalArgumentException, IllegalAccessException {
    System.setProperty("java.library.path", path);
    // set sys_paths to null so that java.library.path will be reevalueted next time it is needed
    final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
    sysPathsField.setAccessible(true);
    sysPathsField.set(null, null);
  }

}