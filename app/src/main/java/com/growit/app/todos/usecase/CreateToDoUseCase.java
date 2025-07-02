package com.growit.app.todos.usecase;

import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.ToDoRepository;
import com.growit.app.todos.domain.dto.CreateToDoCommand;
import com.growit.app.todos.domain.service.ToDoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateToDoUseCase {
  private final ToDoValidator toDoValidator;
  private final ToDoRepository toDoRepository;

  @Transactional
  public String execute(CreateToDoCommand command) {
    toDoValidator.isDateInRange(command.date(), command.goalId());
    toDoValidator.tooManyToDoCreated(command.date(), command.userId(), command.planId());

    ToDo toDo = ToDo.from(command);
    toDoRepository.saveToDo(toDo);
    return toDo.getId();
  }
}
