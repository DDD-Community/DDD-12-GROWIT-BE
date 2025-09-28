package com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source;

import com.growit.app.retrospect.infrastructure.persistence.goalretrospect.source.entity.GoalRetrospectEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface DBGoalRetrospectQueryRepository {
  List<GoalRetrospectEntity> findAllByGoalIdAndCreatedAtBetween(
      String goalId, LocalDateTime start, LocalDateTime end);
}
