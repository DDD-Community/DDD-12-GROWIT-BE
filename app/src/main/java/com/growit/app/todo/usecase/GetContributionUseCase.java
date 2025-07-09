package com.growit.app.todo.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetContributionUseCase {
  private final GoalRepository goalRepository;

  @Transactional
  public void execute(String userId, String goalId) {
    Goal goal = goalRepository.findByIdAndUserId(goalId, userId).orElseThrow();
    goal.getPlans();
  }
}
