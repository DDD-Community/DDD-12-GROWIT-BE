package com.growit.app.todo.infrastructure.persistence.todos;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.RoutineDuration;
import com.growit.app.todo.domain.vo.RepeatType;
import com.growit.app.todo.infrastructure.persistence.todos.source.entity.ToDoEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ToDoDBMapper {
  public ToDoEntity toEntity(ToDo todo) {
    ToDoEntity.ToDoEntityBuilder builder = ToDoEntity.builder()
        .uid(todo.getId())
        .userId(todo.getUserId())
        .goalId(todo.getGoalId())
        .content(todo.getContent())
        .date(todo.getDate())
        .isCompleted(todo.isCompleted())
        .isImportant(todo.isImportant());
    
    if (todo.getRoutine() != null) {
      builder
          .routineStartDate(todo.getRoutine().getDuration().getStartDate())
          .routineEndDate(todo.getRoutine().getDuration().getEndDate())
          .routineRepeatType(todo.getRoutine().getRepeatType());
    }
    
    return builder.build();
  }

  public ToDo toDomain(ToDoEntity entity) {
    if (entity == null) return null;
    
    Routine routine = null;
    if (entity.getRoutineStartDate() != null && entity.getRoutineEndDate() != null && entity.getRoutineRepeatType() != null) {
      RoutineDuration duration = RoutineDuration.of(entity.getRoutineStartDate(), entity.getRoutineEndDate());
      RepeatType repeatType = entity.getRoutineRepeatType();
      routine = Routine.of(duration, repeatType);
    }
    
    return ToDo.builder()
        .id(entity.getUid())
        .goalId(entity.getGoalId())
        .userId(entity.getUserId())
        .content(entity.getContent())
        .date(entity.getDate())
        .isCompleted(entity.isCompleted())
        .isDeleted(entity.getDeletedAt() != null)
        .isImportant(entity.isImportant())
        .routine(routine)
        .build();
  }

  public List<ToDo> toDomainList(List<ToDoEntity> entities) {
    return entities.stream().map(this::toDomain).toList();
  }
}
