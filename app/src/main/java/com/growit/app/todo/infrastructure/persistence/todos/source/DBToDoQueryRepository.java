package com.growit.app.todo.infrastructure.persistence.todos.source;

import com.growit.app.todo.domain.dto.GetCountByDateQueryFilter;
import com.growit.app.todo.domain.dto.GetToDoDateQueryFilter;
import com.growit.app.todo.infrastructure.persistence.todos.source.entity.ToDoEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DBToDoQueryRepository {
  Optional<ToDoEntity> findByUid(String uid);

  List<ToDoEntity> findByPlanIdQuery(String planId);

  List<ToDoEntity> findByUserIdAndDate(String userId, LocalDate today);

  List<ToDoEntity> findByDateFilter(GetToDoDateQueryFilter filter);

  List<ToDoEntity> findByGoalId(String goalId);

  int countByDateQuery(GetCountByDateQueryFilter countByDateQueryFilter);

  int countByUserId(String userId);
}
