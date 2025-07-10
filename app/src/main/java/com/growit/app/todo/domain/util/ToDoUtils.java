package com.growit.app.todo.domain.util;

import static com.growit.app.todo.domain.vo.ToDoStatus.getStatus;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ToDoUtils {
  public static List<ToDo> getNotCompletedToDos(List<ToDo> toDoList) {
    List<ToDo> notCompleted = toDoList.stream().filter(todo -> !todo.isCompleted()).toList();

    if (toDoList.isEmpty()) {
      return null;
    }

    if (!notCompleted.isEmpty()) {
      return notCompleted;
    }

    return List.of();
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
    final int days = 28;
    Map<LocalDate, List<ToDo>> toDoByDate =
        toDoList.stream().collect(Collectors.groupingBy(ToDo::getDate));
    List<ToDoStatus> statusList = new ArrayList<>();
    for (int i = 0; i < days; i++) {
      LocalDate date = goal.getDuration().startDate().plusDays(i);
      List<ToDo> todos = toDoByDate.getOrDefault(date, Collections.emptyList());

      ToDoStatus status = getStatus(todos);
      statusList.add(status);
    }
    return statusList;
  }
}
