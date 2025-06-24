package com.growit.app.goal.domain.goal;

import java.util.Optional;

public interface GoalRepository {
  Optional<Goal> findByIdWithAllRelations(String id);

  void saveGoal(Goal goal);
}
