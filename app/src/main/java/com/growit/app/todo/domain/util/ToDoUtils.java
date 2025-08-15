package com.growit.app.todo.domain.util;

import com.growit.app.todo.domain.ToDo;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

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

  public static List<ToDo> getCompletedToDos(List<ToDo> toDos) {
    return toDos.stream().filter(ToDo::isCompleted).toList();
  }

  public static int calculateToDoCompletedRate(List<ToDo> toDos) {
    if (toDos.isEmpty()) return 0;
    long completedCount = getCompletedToDos(toDos).size();
    return (int) ((completedCount / (double) toDos.size()) * 100);
  }
}
