package com.growit.app.todo.infrastructure.persistence.todos.source;

import com.growit.app.todo.infrastructure.persistence.todos.source.entity.ToDoEntity;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBToDoRepository extends JpaRepository<ToDoEntity, Long>, DBToDoQueryRepository {
  int countByDateAndUserIdAndPlanId(LocalDate date, String userId, String planId);

  int countByDateAndUserIdAndPlanIdAndUidNot(
      LocalDate date, String userId, String planId, String toDoId);
}
