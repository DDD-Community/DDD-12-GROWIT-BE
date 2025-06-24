package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateGoalUseCase {
  private final GoalRepository goalRepository;

  @Transactional
  public String execute(CreateGoalCommand command) {
    // Validate plan contents
    validatePlans(command);
    
    // Create and save goal
    Goal goal = Goal.from(command);
    goalRepository.saveGoal(goal);
    
    return goal.getId();
  }
  
  private void validatePlans(CreateGoalCommand command) {
    if (command.plans() == null || command.plans().isEmpty()) {
      throw new IllegalArgumentException("주간 계획은 필수입니다.");
    }
    
    command.plans().forEach(plan -> {
      if (plan.content() == null || plan.content().trim().isEmpty()) {
        throw new IllegalArgumentException("주간 계획 내용은 필수입니다.");
      }
      if (plan.content().length() > 20) {
        throw new IllegalArgumentException("주간 계획은 20자 이하여야 합니다.");
      }
    });
  }
}