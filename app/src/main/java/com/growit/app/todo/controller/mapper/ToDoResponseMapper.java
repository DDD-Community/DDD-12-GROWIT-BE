package com.growit.app.todo.controller.mapper;

import com.growit.app.todo.controller.dto.response.ToDoResponse;
import com.growit.app.todo.controller.dto.response.WeeklyTodosResponse;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.ToDoResult;
import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    var plan = result.getPlan();
    var planInfo = new ToDoResponse.PlanInfo(plan.getId(), plan.getWeekOfMonth());
    return new ToDoResponse(result.getId(), planInfo);
  }
}
