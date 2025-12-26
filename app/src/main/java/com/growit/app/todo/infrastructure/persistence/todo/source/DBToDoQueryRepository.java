package com.growit.app.todo.infrastructure.persistence.todo.source;

import com.growit.app.todo.domain.dto.GetCountByDateQueryFilter;
import com.growit.app.todo.domain.dto.GetDateRangeQueryFilter;
import com.growit.app.todo.domain.dto.GetToDoDateQueryFilter;
import com.growit.app.todo.infrastructure.persistence.todo.source.entity.ToDoEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DBToDoQueryRepository {
  Optional<ToDoEntity> findByUid(String uid);

  List<ToDoEntity> findByUserIdAndDate(String userId, LocalDate today);

  List<ToDoEntity> findByDateFilter(GetToDoDateQueryFilter filter);

  List<ToDoEntity> findByGoalId(String goalId);

  int countByDateQuery(GetCountByDateQueryFilter countByDateQueryFilter);

  List<ToDoEntity> findAllByUserIdAndCreatedAtBetween(
      String userId, LocalDateTime start, LocalDateTime end);

  List<ToDoEntity> findByUserIdAndDateRange(GetDateRangeQueryFilter filter);
}
