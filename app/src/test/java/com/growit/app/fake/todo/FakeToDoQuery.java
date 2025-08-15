package com.growit.app.fake.todo;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.service.ToDoQuery;
import java.util.List;

public class FakeToDoQuery implements ToDoQuery {
  private final ToDoRepository repository;

  public FakeToDoQuery(ToDoRepository repository) {
    this.repository = repository;
  }

  @Override
  public ToDo getMyToDo(String id, String userId) {
    ToDo todo =
        repository.findById(id).orElseThrow(() -> new NotFoundException("할 일 정보가 존재하지 않습니다."));

    if (!todo.getUserId().equals(userId)) {
      throw new NotFoundException("사용자 정보가 일치하지 않습니다.");
    }

    return todo;
  }

  @Override
  public List<ToDo> getToDosByGoalId(String goalId) {
    return repository.findByGoalId(goalId);
  }
}
