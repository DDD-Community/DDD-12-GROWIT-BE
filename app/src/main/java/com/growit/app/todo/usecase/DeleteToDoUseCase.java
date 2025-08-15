package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.service.GoalStatusUpdater;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.DeleteToDoCommand;
import com.growit.app.todo.domain.service.ToDoQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteToDoUseCase {
  private final ToDoRepository toDoRepository;
  private final GoalStatusUpdater goalStatusUpdater;
  private final ToDoQuery toDoQuery;

  @Transactional
  public void execute(DeleteToDoCommand command) {
    ToDo toDo = toDoQuery.getMyToDo(command.id(), command.userId());
    toDo.deleted();
    toDoRepository.saveToDo(toDo);
    goalStatusUpdater.refreshByToDo(toDo.getGoalId());
  }
}
