package com.growit.app.todo.usecase;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.GetToDoQueryFilter;
import com.growit.app.todo.domain.service.ToDoQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetToDoUseCase {
  private final ToDoQuery todoQuery;

  public ToDo execute(GetToDoQueryFilter query) {
    return todoQuery.getMyToDo(query.id(), query.userId());
  }
}
