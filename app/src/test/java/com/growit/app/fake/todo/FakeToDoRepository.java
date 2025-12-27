package com.growit.app.fake.todo;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetCountByDateQueryFilter;
import com.growit.app.todo.domain.dto.GetDateRangeQueryFilter;
import com.growit.app.todo.domain.dto.GetToDoDateQueryFilter;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  public int countByDateQuery(GetCountByDateQueryFilter filter) {
    return (int)
        store.values().stream()
            .flatMap(List::stream)
            .filter(todo -> !todo.isDeleted())
            .filter(todo -> todo.getDate().equals(filter.date()))
            .filter(todo -> todo.getUserId().equals(filter.userId()))
            .filter(todo -> todo.getGoalId().equals(filter.goalId()))
            .filter(todo -> filter.toDoId().map(id -> !todo.getId().equals(id)).orElse(true))
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
  public List<ToDo> findByUserIdAndDate(String userId, LocalDate today) {
    return store.getOrDefault(userId, Collections.emptyList()).stream()
        .filter(todo -> todo.getDate().equals(today))
        .toList();
  }

  @Override
  public List<ToDo> findByDateFilter(GetToDoDateQueryFilter filter) {
    return List.of();
  }

  @Override
  public List<ToDo> findByGoalId(String goalId) {
    return store.values().stream()
        .flatMap(List::stream)
        .filter(todo -> todo.getGoalId().equals(goalId))
        .filter(todo -> !todo.isDeleted())
        .toList();
  }

  @Override
  public List<ToDo> findAllByUserIdAndCreatedAtBetween(
      String userId, LocalDateTime start, LocalDateTime end) {
    return List.of();
  }

  @Override
  public List<ToDo> findByUserIdAndDateRange(GetDateRangeQueryFilter filter) {
    return store.getOrDefault(filter.userId(), Collections.emptyList()).stream()
        .filter(todo -> !todo.isDeleted())
        .filter(
            todo ->
                !todo.getDate().isBefore(filter.fromDate())
                    && !todo.getDate().isAfter(filter.toDate()))
        .toList();
  }

  @Override
  public List<ToDo> findByRoutineIdAndUserId(String routineId, String userId) {
    return store.getOrDefault(userId, Collections.emptyList()).stream()
        .filter(todo -> todo.getRoutine() != null)
        .filter(todo -> todo.getRoutine().getId().equals(routineId))
        .filter(todo -> !todo.isDeleted())
        .toList();
  }

  @Override
  public List<ToDo> findByRoutineIdAndUserIdAndDateAfter(
      String routineId, String userId, LocalDate date) {
    return store.getOrDefault(userId, Collections.emptyList()).stream()
        .filter(todo -> todo.getRoutine() != null)
        .filter(todo -> todo.getRoutine().getId().equals(routineId))
        .filter(todo -> !todo.getDate().isBefore(date))
        .filter(todo -> !todo.isDeleted())
        .toList();
  }

  @Override
  public void deleteToDo(String id) {
    store.values().forEach(todos -> todos.removeIf(todo -> todo.getId().equals(id)));
  }

  public void clear() {
    store.clear();
  }
}
