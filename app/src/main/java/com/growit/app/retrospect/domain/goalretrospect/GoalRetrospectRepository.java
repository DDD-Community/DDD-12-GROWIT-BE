package com.growit.app.retrospect.domain.goalretrospect;

import java.util.Optional;

public interface GoalRetrospectRepository {
  void save(GoalRetrospect goalRetrospect);

  Optional<GoalRetrospect> findById(String id);

  Optional<GoalRetrospect> findByGoalId(String goalId);
}
