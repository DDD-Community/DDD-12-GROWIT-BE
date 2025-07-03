package com.growit.app.fake.todos;

import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.ToDoRepository;
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
          // 같은 id의 ToDo는 제거(중복 방지)
          todos.removeIf(td -> td.getId().equals(toDo.getId()));
          todos.add(toDo);
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
  public int countByToDoWithToDoId(LocalDate date, String userId, String planId, String toDoId) {
    return (int)
        store.values().stream()
            .flatMap(List::stream)
            .filter(t -> t.getDate().equals(date))
            .filter(t -> t.getUserId().equals(userId))
            .filter(t -> t.getPlanId().equals(planId))
            .filter(t -> !t.getId().equals(toDoId))
            .count();
  }

  @Override
  public Optional<ToDo> findById(String id) {
    return store.values().stream()
        .flatMap(List::stream)
        .filter(todo -> todo.getId().equals(id))
        .findFirst();
  }

  public void clear() {
    store.clear();
  }
}
