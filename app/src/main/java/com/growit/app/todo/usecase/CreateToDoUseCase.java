package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.service.ToDoHandler;
import com.growit.app.todo.domain.service.ToDoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateToDoUseCase {
  private final GoalQuery goalQuery;
  private final ToDoHandler toDoHandler;
  private final ToDoValidator toDoValidator;
  private final ToDoRepository toDoRepository;

  @Transactional
  public ToDoResult execute(CreateToDoCommand command) {
    Goal goal = goalQuery.getMyGoal(command.goalId(), command.userId());
    Plan plan = goal.getPlanByDate(command.date());
    toDoValidator.tooManyToDoCreated(command.date(), command.userId(), plan.getId());
    ToDo toDo = ToDo.from(command, plan.getId());
    toDoRepository.saveToDo(toDo);
    toDoHandler.handle(goal.getId());
    return new ToDoResult(toDo.getId(), plan);
  }
}
