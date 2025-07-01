package com.growit.app.todos.infrastructure.persistence.todos;

import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.ToDoRepository;
import org.springframework.stereotype.Service;

@Service
public class ToDoRepositoryImpl implements ToDoRepository {
  @Override
  public void saveToDo(ToDo toDo) {}
}
