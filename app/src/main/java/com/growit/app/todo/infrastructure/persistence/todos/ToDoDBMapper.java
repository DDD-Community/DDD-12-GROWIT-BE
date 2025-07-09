package com.growit.app.todo.infrastructure.persistence.todos;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.infrastructure.persistence.todos.source.entity.ToDoEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ToDoDBMapper {
  public ToDoEntity toEntity(ToDo todo) {
    return ToDoEntity.builder()
        .uid(todo.getId())
        .userId(todo.getUserId())
        .goalId(todo.getGoalId())
        .planId(todo.getPlanId())
        .content(todo.getContent())
        .date(todo.getDate())
        .isCompleted(todo.isCompleted())
        .build();
  }

  public ToDo toDomain(ToDoEntity entity) {
    if (entity == null) return null;
    return ToDo.builder()
        .id(entity.getUid())
        .goalId(entity.getGoalId())
        .userId(entity.getUserId())
        .planId(entity.getPlanId())
        .content(entity.getContent())
        .date(entity.getDate())
        .isCompleted(entity.isCompleted())
        .isDeleted(entity.getDeletedAt() != null)
        .build();
  }

  public List<ToDo> toDomainList(List<ToDoEntity> entities) {
    return entities.stream().map(this::toDomain).toList();
  }
}
