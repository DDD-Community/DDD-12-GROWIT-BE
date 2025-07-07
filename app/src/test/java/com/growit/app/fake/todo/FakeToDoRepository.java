package com.growit.app.fake.todo;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FakeToDoRepository implements ToDoRepository {
  private final Map<String, List<ToDo>> store = new ConcurrentHashMap<>();

  @Override
  public void saveToDo(ToDo toDo) {
    store.compute(
        toDo.getUserId(),
        (userId, todos) -> {
          if (todos == null) {
            todos = new ArrayList<>();
          }
          todos.removeIf(td -> td.getId().equals(toDo.getId()));
          if (!toDo.isDeleted()) {
            todos.add(toDo);
          }
          return todos;
        });
  }

  @Override
  public int countByToDo(LocalDate date, String userId, String planId) {
    return (int)
        store.getOrDefault(userId, Collections.emptyList()).stream()
            .filter(todo -> todo.getDate().equals(date))
            .filter(todo -> todo.getPlanId().equals(planId))
            .count();
  }

  @Override
  public int countByToDoWithToDoId(LocalDate date, String userId, String planId, String id) {
    return (int)
        store.values().stream()
            .flatMap(List::stream)
            .filter(t -> t.getDate().equals(date))
            .filter(t -> t.getUserId().equals(userId))
            .filter(t -> t.getPlanId().equals(planId))
            .filter(t -> !t.getId().equals(id))
            .count();
  }

  @Override
  public Optional<ToDo> findById(String id) {
    return store.values().stream()
        .flatMap(List::stream)
        .filter(todo -> todo.getId().equals(id))
        .findFirst();
  }

  @Override
  public Map<DayOfWeek, List<ToDo>> groupByPlanId(String userId, String goalId, String planId) {
    List<ToDo> todos =
        store.getOrDefault(userId, Collections.emptyList()).stream()
            .filter(todo -> todo.getGoalId().equals(goalId))
            .filter(todo -> todo.getPlanId().equals(planId))
            .toList();

    Map<DayOfWeek, List<ToDo>> grouped = new LinkedHashMap<>();
    for (DayOfWeek day : DayOfWeek.values()) {
      grouped.put(day, new ArrayList<>());
    }
    for (ToDo todo : todos) {
      grouped.get(todo.getDate().getDayOfWeek()).add(todo);
    }
    return grouped;
  }

  public void clear() {
    store.clear();
  }
}
