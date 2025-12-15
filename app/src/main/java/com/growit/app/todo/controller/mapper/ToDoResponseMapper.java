package com.growit.app.todo.controller.mapper;

import com.growit.app.todo.controller.dto.response.ToDoResponse;
import com.growit.app.todo.controller.dto.response.TodoCountByDateResponse;
import com.growit.app.todo.controller.dto.response.ToDoWithGoalResponse;
import com.growit.app.todo.controller.dto.response.WeeklyTodosResponse;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.usecase.dto.TodoCountByDateDto;
import com.growit.app.todo.usecase.dto.ToDoWithGoalDto;
import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ToDoResponseMapper {
  public Map<String, List<WeeklyTodosResponse>> toWeeklyPlanResponse(
      Map<DayOfWeek, List<ToDo>> grouped) {
    Map<String, List<WeeklyTodosResponse>> result = new LinkedHashMap<>();
    for (DayOfWeek day : DayOfWeek.values()) {
      result.put(
          day.name(),
          grouped.getOrDefault(day, List.of()).stream().map(WeeklyTodosResponse::from).toList());
    }
    return result;
  }

  public ToDoResponse toToDoResponse(ToDoResult result) {
    return new ToDoResponse(result.getId(), null);
  }

  public List<ToDoWithGoalResponse> toToDoWithGoalResponseList(List<ToDoWithGoalDto> dtos) {
    return dtos.stream()
        .map(this::toToDoWithGoalResponse)
        .collect(Collectors.toList());
  }

  private ToDoWithGoalResponse toToDoWithGoalResponse(ToDoWithGoalDto dto) {
    ToDoWithGoalResponse.ToDoInfo todoInfo = ToDoWithGoalResponse.ToDoInfo.builder()
        .id(dto.getTodo().getId())
        .goalId(dto.getTodo().getGoalId())
        .date(dto.getTodo().getDate().toString())
        .content(dto.getTodo().getContent())
        .important(dto.getTodo().isImportant())
        .completed(dto.getTodo().isCompleted())
        .routine(dto.getTodo().getRoutine())
        .build();

    ToDoWithGoalResponse.GoalInfo goalInfo = null;
    if (dto.getGoal() != null) {
      goalInfo = ToDoWithGoalResponse.GoalInfo.builder()
          .id(dto.getGoal().getId())
          .name(dto.getGoal().getName())
          .build();
    }

    return ToDoWithGoalResponse.builder()
        .todo(todoInfo)
        .goal(goalInfo)
        .build();
  }

  public List<TodoCountByDateResponse> toTodoCountByDateResponseList(List<TodoCountByDateDto> dtos) {
    return dtos.stream()
        .map(this::toTodoCountByDateResponse)
        .collect(Collectors.toList());
  }

  private TodoCountByDateResponse toTodoCountByDateResponse(TodoCountByDateDto dto) {
    List<TodoCountByDateResponse.GoalTodoCount> goalCounts = dto.getGoalCounts().stream()
        .map(gc -> TodoCountByDateResponse.GoalTodoCount.builder()
            .id(gc.getGoalId())
            .todoCount(gc.getTodoCount())
            .build())
        .collect(Collectors.toList());

    return TodoCountByDateResponse.builder()
        .date(dto.getDate().toString())
        .goals(goalCounts)
        .build();
  }
}
