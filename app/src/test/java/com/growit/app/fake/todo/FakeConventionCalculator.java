package com.growit.app.fake.todo;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.service.ConventionCalculator;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FakeConventionCalculator implements ConventionCalculator {

  @Override
  public List<ToDoStatus> getContribution(Goal goal, List<ToDo> toDoList) {
    LocalDate start = goal.getDuration().startDate();
    LocalDate today = LocalDate.now();

    Map<LocalDate, List<ToDo>> toDoByDate =
        toDoList.stream().collect(Collectors.groupingBy(ToDo::getDate));

    List<ToDoStatus> statusList = new ArrayList<>();

    for (LocalDate date = start; !date.isAfter(today); date = date.plusDays(1)) {
      statusList.add(getStatusForDate(date, today, toDoByDate));
    }

    return statusList;
  }

  private static ToDoStatus getStatusForDate(
      LocalDate date, LocalDate today, Map<LocalDate, List<ToDo>> toDoByDate) {
    if (date.isAfter(today)) {
      return ToDoStatus.NONE;
    }
    List<ToDo> todos = toDoByDate.getOrDefault(date, Collections.emptyList());
    return ToDoStatus.getStatus(todos);
  }
}
