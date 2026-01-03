package com.growit.app.todo.controller.mapper;

import com.growit.app.todo.controller.dto.response.GoalDto;
import com.growit.app.todo.controller.dto.response.RoutineDto;
import com.growit.app.todo.controller.dto.response.ToDoResponse;
import com.growit.app.todo.controller.dto.response.ToDoWithGoalResponse;
import com.growit.app.todo.controller.dto.response.TodoCountByDateResponse;
import com.growit.app.todo.controller.dto.response.TodoDto;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.usecase.dto.ToDoWithGoalDto;
import com.growit.app.todo.usecase.dto.TodoCountByDateDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ToDoResponseMapper {
  public ToDoResponse toToDoResponse(ToDoResult result) {
    return new ToDoResponse(result.getId());
  }

  public TodoDto toTodoDto(ToDo todo) {
    return TodoDto.builder()
        .id(todo.getId())
        .goalId(todo.getGoalId())
        .date(todo.getDate().toString())
        .content(todo.getContent())
        .important(todo.isImportant())
        .completed(todo.isCompleted())
        .routine(toRoutineDto(todo.getRoutine()))
        .build();
  }

  public List<ToDoWithGoalResponse> toToDoWithGoalResponseList(List<ToDoWithGoalDto> dtos) {
    return dtos.stream().map(this::toToDoWithGoalResponse).toList();
  }

  private ToDoWithGoalResponse toToDoWithGoalResponse(ToDoWithGoalDto dto) {
    TodoDto todoDto = toTodoDto(dto.getTodo());

    GoalDto goalDto = null;
    if (dto.getGoal() != null) {
      goalDto = GoalDto.builder().id(dto.getGoal().getId()).name(dto.getGoal().getName()).build();
    }

    return ToDoWithGoalResponse.builder().todo(todoDto).goal(goalDto).build();
  }

  public List<TodoCountByDateResponse> toTodoCountByDateResponseList(
      List<TodoCountByDateDto> dtos) {
    return dtos.stream().map(this::toTodoCountByDateResponse).toList();
  }

  private TodoCountByDateResponse toTodoCountByDateResponse(TodoCountByDateDto dto) {
    List<TodoCountByDateResponse.GoalTodoCount> goalCounts =
        dto.getGoalCounts().stream()
            .map(
                gc ->
                    TodoCountByDateResponse.GoalTodoCount.builder()
                        .id(gc.getGoalId())
                        .todoCount(gc.getTodoCount())
                        .build())
            .toList();

    return TodoCountByDateResponse.builder()
        .date(dto.getDate().toString())
        .goals(goalCounts)
        .build();
  }

  private RoutineDto toRoutineDto(Routine routine) {
    if (routine == null) {
      return null;
    }

    RoutineDto.DurationDto durationDto = null;
    if (routine.getDuration() != null) {
      durationDto =
          RoutineDto.DurationDto.builder()
              .startDate(routine.getDuration().getStartDate())
              .endDate(routine.getDuration().getEndDate())
              .build();
    }

    return RoutineDto.builder()
        .duration(durationDto)
        .repeatType(routine.getRepeatType().name())
        .build();
  }
}
