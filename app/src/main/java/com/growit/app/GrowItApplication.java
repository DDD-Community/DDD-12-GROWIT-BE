package com.growit.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GrowItApplication {
  public static void main(String[] args) {
    SpringApplication.run(GrowItApplication.class, args);
  }
}
