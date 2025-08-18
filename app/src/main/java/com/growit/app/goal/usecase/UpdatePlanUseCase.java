package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.UpdatePlanCommand;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdatePlanUseCase {
  private final GoalRepository goalRepository;
  private final GoalQuery goalQuery;

  @Transactional
  public void execute(UpdatePlanCommand command) {
    Goal goal = goalQuery.getMyGoal(command.goalId(), command.userId());
    Plan plan = goal.getPlanByPlanId(command.planId());
    plan.updateByPlan(command.content());
    goalRepository.saveGoal(goal);
  }
}
