package com.growit.app.todo.infrastructure.persistence.todo;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.RoutineDuration;
import com.growit.app.todo.infrastructure.persistence.todo.source.entity.RoutineEntity;
import com.growit.app.todo.infrastructure.persistence.todo.source.entity.ToDoEntity;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ToDoDBMapper {

  public ToDoEntity toEntity(ToDo todo, String routineId) {
    return ToDoEntity.builder()
        .uid(todo.getId())
        .userId(todo.getUserId())
        .goalId(todo.getGoalId())
        .content(todo.getContent())
        .date(todo.getDate())
        .isCompleted(todo.isCompleted())
        .isImportant(todo.isImportant())
        .routineId(routineId)
        .build();
  }

  public ToDo toDomain(ToDoEntity entity, Routine routine) {
    if (entity == null) return null;

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

  public RoutineEntity toRoutineEntity(Routine routine, String uid, String userId) {
    return RoutineEntity.builder()
        .uid(uid)
        .userId(userId)
        .startDate(routine.getDuration().getStartDate())
        .endDate(routine.getDuration().getEndDate())
        .repeatType(routine.getRepeatType())
        .repeatDays(
            routine.getRepeatDays() != null
                ? routine.getRepeatDays().stream().map(Enum::name).collect(Collectors.joining(","))
                : null)
        .build();
  }

  public Routine routineEntityToDomain(RoutineEntity entity) {
    RoutineDuration duration = RoutineDuration.of(entity.getStartDate(), entity.getEndDate());

    List<DayOfWeek> repeatDays = null;
    if (entity.getRepeatDays() != null && !entity.getRepeatDays().isEmpty()) {
      repeatDays =
          Arrays.stream(entity.getRepeatDays().split(","))
              .map(String::trim)
              .map(DayOfWeek::valueOf)
              .collect(Collectors.toList());
    }

    return new Routine(entity.getUid(), duration, entity.getRepeatType(), repeatDays);
  }
}
