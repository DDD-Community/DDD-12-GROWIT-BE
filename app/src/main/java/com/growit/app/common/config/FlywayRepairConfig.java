package com.growit.app.common.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * One-shot hotfix to realign flyway_schema_history checksums for V22/V23 on the prod database.
 *
 * <p>Remove in the immediate follow-up PR once the next prod deploy boots successfully. Do not keep
 * this on main permanently: an always-on repair silently masks accidental migration edits.
 *
 * <p>Context: 2026-05-24 dev exhibited a FlywayValidateException at V22/V23 (fixed in PR #451 on
 * develop). main still ships the IF NOT EXISTS variant of V22/V23, so the prod
 * flyway_schema_history checksums may not match the jar SQL the next time cd.yml runs. Holding this
 * PR ready means the fix is one merge + release publish away if prod fails to boot.
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
