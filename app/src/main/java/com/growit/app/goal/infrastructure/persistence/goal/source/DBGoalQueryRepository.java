package com.growit.app.goal.infrastructure.persistence.goal.source;

import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.util.Optional;

public interface DBGoalQueryRepository {
  Optional<GoalEntity> findWithPlansByUid(String uid);
}
