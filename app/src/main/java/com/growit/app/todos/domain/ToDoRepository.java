package com.growit.app.todos.domain;

import java.time.LocalDate;
import java.util.Optional;

public interface ToDoRepository {
  void saveToDo(ToDo toDo);

  int countByToDo(LocalDate date, String userId, String planId);

  int countByToDo(LocalDate date, String userId, String planId, String toDoId);

  Optional<ToDo> findById(String id);
}
