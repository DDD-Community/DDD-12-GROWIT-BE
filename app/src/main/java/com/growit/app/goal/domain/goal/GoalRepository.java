package com.growit.app.goal.domain.goal;

import java.util.List;

public interface GoalRepository {
  List<Goal> findByUserId(String userId);

  void saveGoal(Goal goal);
}
