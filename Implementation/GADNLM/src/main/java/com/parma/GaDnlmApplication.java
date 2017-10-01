package com.parma;

import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GaDnlmApplication {

  public static void main(String[] args) {
    System.out.println(Core.NATIVE_LIBRARY_NAME);
    SpringApplication.run(GaDnlmApplication.class, args);           
  }
  
}
