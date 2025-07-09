package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.service.ToDoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateToDoUseCase {
  private final GoalQuery goalQuery;
  private final ToDoValidator toDoValidator;
  private final ToDoRepository toDoRepository;

  @Transactional
  public String execute(CreateToDoCommand command) {
    Goal goal = goalQuery.getMyGoal(command.goalId(), command.userId());
    toDoValidator.isDateInRange(command.date(), goal.getDuration().startDate());
    toDoValidator.tooManyToDoCreated(command.date(), command.userId(), command.planId());

    ToDo toDo = ToDo.from(command);
    toDoRepository.saveToDo(toDo);
    return toDo.getId();
  }
}
