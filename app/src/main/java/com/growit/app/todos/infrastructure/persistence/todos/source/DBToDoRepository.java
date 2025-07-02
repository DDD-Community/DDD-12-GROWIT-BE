package com.growit.app.todos.infrastructure.persistence.todos.source;

import com.growit.app.todos.infrastructure.persistence.todos.source.entity.ToDoEntity;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBToDoRepository extends JpaRepository<ToDoEntity, Long> {
  Optional<ToDoEntity> findByUid(String uid);

  int countByDateAndUserId(LocalDate date, String userId);
}
