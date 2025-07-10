package com.growit.app.fake.goal;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.service.GoalQuery;

public class FakeGoalQuery implements GoalQuery {
  private final GoalRepository repository;

  public FakeGoalQuery(GoalRepository repository) {
    this.repository = repository;
  }

  @Override
  public Goal getMyGoal(String id, String userId) throws NotFoundException {
    return repository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException(""));
  }
}
