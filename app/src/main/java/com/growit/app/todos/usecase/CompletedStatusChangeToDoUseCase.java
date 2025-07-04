package com.growit.app.todos.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.ToDoRepository;
import com.growit.app.todos.domain.dto.CompletedStatusChangeCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompletedStatusChangeToDoUseCase {
  private final ToDoRepository toDoRepository;

  @Transactional
  public void execute(CompletedStatusChangeCommand command) {
    ToDo toDo =
        toDoRepository
            .findById(command.id())
            .orElseThrow(() -> new NotFoundException("할 일 정보가 존재하지 않습니다."));
    toDo.isCompleted(command.isCompleted());
    toDoRepository.setIsCompleted(command.id(), command.isCompleted());
  }
}
