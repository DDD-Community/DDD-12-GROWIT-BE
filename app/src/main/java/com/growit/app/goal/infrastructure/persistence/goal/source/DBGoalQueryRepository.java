package com.growit.app.goal.infrastructure.persistence.goal.source;

import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.util.List;
import java.util.Optional;

public interface DBGoalQueryRepository {
  List<GoalEntity> findByUserId(String userId);

  Optional<GoalEntity> findByIdAndUserId(String id, String userId);
}
