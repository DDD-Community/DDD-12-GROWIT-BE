package com.growit.app.common.health;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/** Initializes JPA and verifies the database connection during scheduled Lambda warm-up. */
@Component("warmup")
@RequiredArgsConstructor
public class WarmupHealthIndicator implements HealthIndicator {
  private final EntityManager entityManager;

  @Override
  public Health health() {
    try {
      entityManager.createNativeQuery("SELECT 1").getSingleResult();
      return Health.up().build();
    } catch (RuntimeException exception) {
      return Health.down(exception).build();
    }
  }
}
