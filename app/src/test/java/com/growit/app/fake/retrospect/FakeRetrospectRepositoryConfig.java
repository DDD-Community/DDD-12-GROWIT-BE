package com.growit.app.fake.retrospect;

import com.growit.app.goal.domain.retrospect.RetrospectRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FakeRetrospectRepositoryConfig {
  @Bean
  public RetrospectRepository retrospectRepository() {
    return new FakeRetrospectRepository();
  }
}
