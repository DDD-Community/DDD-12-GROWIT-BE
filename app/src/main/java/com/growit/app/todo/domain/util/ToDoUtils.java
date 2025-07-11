package com.growit.app.todo.domain.util;

import static com.growit.app.todo.domain.vo.ToDoStatus.getStatus;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ToDoUtils {
  public static List<ToDo> getNotCompletedToDos(List<ToDo> toDoList) {
    return toDoList.stream().filter(todo -> !todo.isCompleted()).toList();
  }

  public static Map<DayOfWeek, List<ToDo>> groupByDayOfWeek(List<ToDo> toDoList) {
    Map<DayOfWeek, List<ToDo>> map =
        toDoList.stream()
            .collect(
                Collectors.groupingBy(
                    todo -> todo.getDate().getDayOfWeek(),
                    LinkedHashMap::new,
                    Collectors.toList()));

    for (DayOfWeek day : DayOfWeek.values()) {
      map.putIfAbsent(day, List.of());
    }

    return map;
  }

  public static List<ToDoStatus> getContribution(Goal goal, List<ToDo> toDoList) {
    final int CONTRIBUTION_DAYS = 28;
    LocalDate today = LocalDate.now();

    Map<LocalDate, List<ToDo>> toDoByDate =
        toDoList.stream().collect(Collectors.groupingBy(ToDo::getDate));

    return IntStream.range(0, CONTRIBUTION_DAYS)
        .mapToObj(i -> goal.getDuration().startDate().plusDays(i))
        .map(date -> getStatusForDate(date, today, toDoByDate))
        .collect(Collectors.toList());
  }

  private static ToDoStatus getStatusForDate(
      LocalDate date, LocalDate today, Map<LocalDate, List<ToDo>> toDoByDate) {
    if (date.isAfter(today)) {
      return ToDoStatus.NONE;
    }
    List<ToDo> todos = toDoByDate.getOrDefault(date, Collections.emptyList());
    return getStatus(todos);
  }

  public static List<ToDo> getCompletedToDos(List<ToDo> toDos) {
    return toDos.stream().filter(ToDo::isCompleted).toList();
  }
}
