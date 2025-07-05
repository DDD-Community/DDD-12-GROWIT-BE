package com.growit.app.fake.todo;

import com.growit.app.todo.domain.ToDoRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FakeToDoRepositoryConfig {
  @Bean
  public ToDoRepository toDoRepository() {
    return new FakeToDoRepository();
  }
}
