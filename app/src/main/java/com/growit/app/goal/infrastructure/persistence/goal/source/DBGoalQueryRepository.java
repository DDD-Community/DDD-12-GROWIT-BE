package com.growit.app.goal.infrastructure.persistence.goal.source;

import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.util.List;

public interface DBGoalQueryRepository {
  List<GoalEntity> findWithPlansByUserId(String userId);
}
