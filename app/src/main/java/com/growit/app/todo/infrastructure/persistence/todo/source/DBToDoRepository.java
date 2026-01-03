package com.growit.app.todo.infrastructure.persistence.todo.source;

import com.growit.app.todo.infrastructure.persistence.todo.source.entity.ToDoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBToDoRepository extends JpaRepository<ToDoEntity, Long>, DBToDoQueryRepository {}
