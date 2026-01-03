package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.DeleteGoalCommand;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteGoalUseCase {
  private final GoalRepository goalRepository;
  private final GoalQuery goalQuery;

  @CacheEvict(value = "goalCache", key = "#command.userId")
  @Transactional
  public void execute(DeleteGoalCommand command) {
    Goal goal = goalQuery.getMyGoal(command.id(), command.userId());
    goal.delete();
    goalRepository.saveGoal(goal);
  }
}
