package com.growit.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GrowItApplication {
  public static void main(String[] args) {
    SpringApplication.run(GrowItApplication.class, args);
  }
}
