package com.growit.app.todo.usecase;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.CompletedStatusChangeCommand;
import com.growit.app.todo.domain.service.ToDoQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompletedStatusChangeToDoUseCase {
  private final ToDoRepository toDoRepository;
  private final ToDoQuery toDoQuery;

  @Transactional
  public void execute(CompletedStatusChangeCommand command) {
    ToDo toDo = toDoQuery.getMyToDo(command.id(), command.userId());

    // Always update completion status
    if(command.completed() != null) {
      toDo.updateIsCompleted(command.completed());
    }

    // Only update importance if provided (not null)
    if (command.important() != null) {
      toDo.updateIsImportant(command.important());
    }

    toDoRepository.saveToDo(toDo);
  }
}
