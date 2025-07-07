package com.growit.app.todo.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ToDoRepository {
  void saveToDo(ToDo toDo);

  int countByToDo(LocalDate date, String userId, String planId);

  int countByToDoWithToDoId(LocalDate date, String userId, String planId, String id);

  Optional<ToDo> findById(String id);

  Map<DayOfWeek, List<ToDo>> groupByPlanId(String goalId, String userId, String planId);
}
