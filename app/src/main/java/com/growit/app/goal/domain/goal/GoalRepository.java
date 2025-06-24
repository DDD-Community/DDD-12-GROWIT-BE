package com.growit.app.goal.domain.goal;

import java.util.Optional;

public interface GoalRepository {
  Optional<Goal> findByUserId(String userId);


  void saveGoal(Goal goal);
}
