package com.growit.app.retrospect.domain.goalretrospect;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GoalRetrospectRepository {
  void save(GoalRetrospect goalRetrospect);

  Optional<GoalRetrospect> findById(String id);

  Optional<GoalRetrospect> findByGoalId(String goalId);

  List<GoalRetrospect> findAllByGoalIdAndCreatedAtBetween(
      String goalId, LocalDateTime start, LocalDateTime end);
}
