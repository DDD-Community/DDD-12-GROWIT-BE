package com.growit.app.common.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayRepairConfig {

  @Bean
  public FlywayMigrationStrategy repairAndMigrate() {
    return (flyway) -> {
      flyway.repair();
      flyway.migrate();
    };
  }
}
