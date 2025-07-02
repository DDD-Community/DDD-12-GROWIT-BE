package com.growit.app.todos.domain;

import java.time.LocalDate;

public interface ToDoRepository {
  void saveToDo(ToDo toDo);

  int countByToDo(LocalDate date, String userId);
}
