package com.growit.fake.retrospect;

import com.growit.retrospect.domain.RetrospectRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FakeRetrospectRepositoryConfig {
  @Bean
  public RetrospectRepository retrospectRepository() {
    return new FakeRetrospectRepository();
  }
}