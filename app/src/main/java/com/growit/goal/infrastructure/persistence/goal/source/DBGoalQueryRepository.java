package com.growit.goal.infrastructure.persistence.goal.source;

import com.growit.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.util.List;

public interface DBGoalQueryRepository {
  List<GoalEntity> findByUserId(String userId);
}
