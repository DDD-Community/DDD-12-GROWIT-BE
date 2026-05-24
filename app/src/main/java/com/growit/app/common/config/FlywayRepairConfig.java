package com.growit.app.common.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * One-shot hotfix to realign flyway_schema_history checksums for V22/V23.
 *
 * <p>Remove in the immediate follow-up PR once dev boot succeeds. Do not keep this on develop
 * permanently: an always-on repair masks accidental migration edits.
 *
 * <p>Context: 2026-05-24 dev deploy failure caused by checksum mismatch between the already-applied
 * V22/V23 in flyway_schema_history and the SQL shipped in the new jar.
 */
@Configuration
public class FlywayRepairConfig {

  @Bean
  public FlywayMigrationStrategy flywayMigrationStrategy() {
    return flyway -> {
      flyway.repair();
      flyway.migrate();
    };
  }
}
