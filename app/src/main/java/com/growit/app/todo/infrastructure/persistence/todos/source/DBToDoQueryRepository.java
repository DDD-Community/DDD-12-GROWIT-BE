package com.growit.app.todo.infrastructure.persistence.todos.source;

import com.growit.app.todo.infrastructure.persistence.todos.source.entity.ToDoEntity;

import java.util.List;
import java.util.Optional;

public interface DBToDoQueryRepository {
  Optional<ToDoEntity> findByUid(String uid);

  List<ToDoEntity> findByUserIdAndGoalIdAndPlanId(String userId, String goalId, String planId);
}
