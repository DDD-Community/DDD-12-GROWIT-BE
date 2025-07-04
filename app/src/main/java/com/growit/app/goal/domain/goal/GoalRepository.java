package com.growit.app.goal.domain.goal;

import java.util.List;
import java.util.Optional;

public interface GoalRepository {
  List<Goal> findAllByUserIdAndDeletedAtIsNull(String userId);

  void saveGoal(Goal goal);

  Optional<Goal> findById(String goalId);
}
