package com.growit.app.todo.domain;

import com.growit.app.todo.domain.dto.GetCountByDateQueryFilter;
import com.growit.app.todo.domain.dto.GetToDoDateQueryFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ToDoRepository {
  void saveToDo(ToDo toDo);

  int countByDateQuery(GetCountByDateQueryFilter countByDateQueryFilter);

  Optional<ToDo> findById(String id);

  List<ToDo> findByPlanId(String planId);

  List<ToDo> findByUserIdAndDate(String userId, LocalDate today);

  List<ToDo> findByGoalId(String goalId);

  List<ToDo> findByDateFilter(GetToDoDateQueryFilter filter);

  List<ToDo> findAllByUserIdAndCreatedAtBetween(String userId, LocalDateTime start, LocalDateTime end);
}
