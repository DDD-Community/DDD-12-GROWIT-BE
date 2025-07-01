package com.growit.goal.usecase;

import com.growit.goal.domain.goal.Goal;
import com.growit.goal.domain.goal.GoalRepository;
import com.growit.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.goal.domain.goal.service.GoalValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateGoalUseCase {
  private final GoalValidator goalValidator;
  private final GoalRepository goalRepository;

  @Transactional
  public String execute(CreateGoalCommand command) {
    goalValidator.checkPlans(command.duration(), command.plans());

    Goal goal = Goal.from(command);
    goalRepository.saveGoal(goal);

    return goal.getId();
  }
}
