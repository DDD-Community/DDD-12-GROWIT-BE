package com.growit.app.todo.controller.mapper;

import com.growit.app.todo.controller.dto.response.WeeklyPlanResponse;
import com.growit.app.todo.domain.ToDo;
import java.time.DayOfWeek;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ToDoResponseMapper {
  public Map<String, List<WeeklyPlanResponse>> toWeeklyPlanResponse(
      Map<DayOfWeek, List<ToDo>> grouped) {
    Map<String, List<WeeklyPlanResponse>> result = new LinkedHashMap<>();
    for (DayOfWeek day : DayOfWeek.values()) {
      result.put(
          day.name(),
          grouped.getOrDefault(day, List.of()).stream().map(WeeklyPlanResponse::from).toList());
    }
    return result;
  }
}
