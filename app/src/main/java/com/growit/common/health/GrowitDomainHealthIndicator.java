package com.growit.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class GrowitDomainHealthIndicator implements HealthIndicator {

  @Override
  public Health health() {
    boolean customCheck = true; // 여기에 서비스 상태 확인 로직 삽입
    if (customCheck) {
      return Health.up().withDetail("custom", "서비스 정상 작동 중").build();
    } else {
      return Health.down().withDetail("custom", "문제 발생").build();
    }
  }
}
