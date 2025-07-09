package com.growit.app.todo.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ToDoRepository {
  void saveToDo(ToDo toDo);

  int countByToDo(LocalDate date, String userId, String planId);

  int countByToDoWithToDoId(LocalDate date, String userId, String planId, String id);

  Optional<ToDo> findById(String id);

  List<ToDo> findByPlanId(String planId);

  List<ToDo> findByUserIdAndDate(String userId, LocalDate today);
}
