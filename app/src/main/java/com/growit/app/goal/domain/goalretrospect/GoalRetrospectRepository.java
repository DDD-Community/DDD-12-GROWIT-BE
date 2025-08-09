package com.growit.app.goal.domain.goalretrospect;

import java.util.List;
import java.util.Optional;

public interface GoalRetrospectRepository {
  void save(GoalRetrospect goalRetrospect);

  Optional<GoalRetrospect> findById(String id);

  Optional<GoalRetrospect> findByGoalId(String goalId);

  List<GoalRetrospect> findAllByGoalId(String goalId);
}
