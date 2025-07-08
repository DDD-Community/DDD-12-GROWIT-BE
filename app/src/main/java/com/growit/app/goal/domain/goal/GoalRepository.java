package com.growit.app.goal.domain.goal;

import java.util.List;
import java.util.Optional;

public interface GoalRepository {
  List<Goal> findAllByUserId(String userId);

  void saveGoal(Goal goal);

  Optional<Goal> findById(String goalId);

  Optional<Goal> findByIdAndUserId(String id, String userId);
}
