package com.growit.app.todos.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.ToDoRepository;
import com.growit.app.todos.domain.dto.UpdateToDoCommand;
import com.growit.app.todos.domain.service.ToDoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateToDoUseCase {
  private final ToDoValidator toDoValidator;
  private final ToDoRepository toDoRepository;

  @Transactional
  public void execute(UpdateToDoCommand command) {
    ToDo toDo =
        toDoRepository
            .findById(command.id())
            .orElseThrow(() -> new NotFoundException("할 일 정보가 존재하지 않습니다."));

    toDoValidator.isDateInRange(command.date());
    toDoValidator.tooManyToDoCreated(command.date(), command.userId(), command.planId());
    toDoValidator.checkContent(command.content());

    toDo.updateBy(command);
    toDoRepository.saveToDo(toDo);
  }
}
