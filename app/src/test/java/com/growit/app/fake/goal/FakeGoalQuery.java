package com.growit.app.fake.goal;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import java.util.List;

public class FakeGoalQuery implements GoalQuery {
  private final GoalRepository repository;

  public FakeGoalQuery(GoalRepository repository) {
    this.repository = repository;
  }

  @Override
  public Goal getMyGoal(String id, String userId) throws NotFoundException {
    return repository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException(""));
  }

  @Override
  public List<Goal> getFinishedGoalsByYear(String userId, int year) {
    return List.of();
  }
}
