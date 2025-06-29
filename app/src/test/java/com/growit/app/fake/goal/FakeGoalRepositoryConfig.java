package com.growit.app.fake.goal;

import com.growit.app.goal.domain.goal.GoalRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FakeGoalRepositoryConfig {
  @Bean
  public GoalRepository goalRepository() {
    return new FakeGoalRepository();
  }
}
