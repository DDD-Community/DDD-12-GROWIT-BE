package com.growit.app.todo.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.service.ToDoQuery;
import com.growit.app.todo.domain.service.ToDoValidator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateToDoUseCase {
  private final ToDoQuery toDoQuery;
  private final ToDoValidator toDoValidator;
  private final ToDoRepository toDoRepository;
  private final GoalRepository goalRepository;

  @Transactional
  public void execute(UpdateToDoCommand command) {
    ToDo toDo = toDoQuery.getMyToDo(command.id(), command.userId());

    Goal goal =
        goalRepository
            .findById(toDo.getGoalId())
            .orElseThrow(() -> new NotFoundException("목표가 존재하지 않습니다."));
    Optional<Plan> p = goal.filterByDate(command.date());
    String planId = p.map(Plan::getId).orElseThrow(() -> new NotFoundException("일치하는 날짜가 없습니다."));
    String goalId = goal.getId();

    toDoValidator.isDateInRange(command.date(), goalId);
    toDoValidator.tooManyToDoUpdated(command.date(), command.userId(), planId, toDo.getId());

    toDo.updateBy(command, planId);
    toDoRepository.saveToDo(toDo);
  }
}
