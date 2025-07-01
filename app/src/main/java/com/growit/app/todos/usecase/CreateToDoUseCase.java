package com.growit.app.todos.usecase;

import com.growit.app.todos.domain.CreateToDoCommand;
import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.ToDoRepository;
import com.growit.app.todos.domain.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateToDoUseCase {
  private final ToDoService toDoService;
  private final ToDoRepository toDoRepository;

  @Transactional
  public String execute(CreateToDoCommand command) {
    toDoService.isDateInRange(command.date());
    ToDo toDo = ToDo.from(command);
    toDoRepository.saveToDo(toDo);
    return toDo.getId();
  }
}
