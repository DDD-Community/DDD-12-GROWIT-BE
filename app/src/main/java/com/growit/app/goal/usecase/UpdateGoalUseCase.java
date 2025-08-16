package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateGoalUseCase {
  private final GoalRepository goalRepository;
  private final GoalQuery goalQuery;

  @Transactional
  public void execute(UpdateGoalCommand command) {
    final Goal goal = goalQuery.getMyGoal(command.id(), command.userId());
    goal.updateByCommand(command);

    goalRepository.saveGoal(goal);
  }
}
