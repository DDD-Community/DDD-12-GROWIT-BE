package com.growit.app.todos.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.ToDoRepository;
import com.growit.app.todos.domain.dto.UpdateToDoCommand;
import com.growit.app.todos.domain.service.ToDoValidator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateToDoUseCase {
  private final ToDoValidator toDoValidator;
  private final ToDoRepository toDoRepository;
  private final GoalRepository goalRepository;

  @Transactional
  public void execute(UpdateToDoCommand command) {
    ToDo toDo =
        toDoRepository
            .findById(command.id())
            .orElseThrow(() -> new NotFoundException("할 일 정보가 존재하지 않습니다."));

    Goal goal =
        goalRepository
            .findById(toDo.getGoalId())
            .orElseThrow(() -> new NotFoundException("목표가 존재하지 않습니다."));
    Optional<Plan> p = goal.filterByDate(command.date());
    String planId = p.map(Plan::getId).orElse(null);
    String goalId = goal.getId();

    toDoValidator.isDateInRange(command.date(), goalId);
    toDoValidator.tooManyToDoUpdated(command.date(), command.userId(), planId, toDo.getId());

    toDo.updateBy(command, planId);
    toDoRepository.saveToDo(toDo);
  }
}
