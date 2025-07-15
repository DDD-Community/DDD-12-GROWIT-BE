package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
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

  //  private final ConventionCalculator conventionCalculator;

  @Transactional
  public void execute(UpdateToDoCommand command) {
    ToDo toDo = toDoQuery.getMyToDo(command.id(), command.userId());
    Goal goal = goalQuery.getMyGoal(toDo.getGoalId(), command.userId());
    Plan plan = goal.getPlanByDate(command.date());

    //    conventionCalculator.isDateInRange(command.date());
    toDoValidator.tooManyToDoUpdated(command.date(), command.userId(), plan.getId(), toDo.getId());

    toDo.updateBy(command, plan.getId());

    toDoRepository.saveToDo(toDo);
  }
}
