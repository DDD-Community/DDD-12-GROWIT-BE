package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.service.ToDoQuery;
import com.growit.app.todo.domain.service.ToDoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateToDoUseCase {
  private final ToDoQuery toDoQuery;
  private final ToDoValidator toDoValidator;
  private final ToDoRepository toDoRepository;
  private final GoalQuery goalQuery;

  @Transactional
  public ToDoResult execute(UpdateToDoCommand command) {
    ToDo toDo = toDoQuery.getMyToDo(command.id(), command.userId());
    if(command.goalId() != null) {
      goalQuery.getMyGoal(command.goalId(), command.userId());
    }

    if (toDo.getGoalId() != null) {
      Goal goal = goalQuery.getMyGoal(toDo.getGoalId(), command.userId());
      toDoValidator.tooManyToDoUpdated(
          command.date(), command.userId(), goal.getId(), toDo.getId());
    }

    toDo.updateBy(command);
    toDoRepository.saveToDo(toDo);

    return new ToDoResult(toDo.getId());
  }
}
