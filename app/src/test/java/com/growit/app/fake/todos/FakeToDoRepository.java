package com.growit.app.fake.todos;

import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.ToDoRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
  public int countByToDo(LocalDate date, String userId) {
    List<ToDo> list = store.getOrDefault(userId, Collections.emptyList());
    return (int) list.stream().filter(todo -> todo.getDate().equals(date)).count();
  }

  public void clear() {
    store.clear();
  }
}
