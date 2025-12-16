package com.growit.app.goal.infrastructure.persistence.goal.source;

import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DBGoalQueryRepository {
  List<GoalEntity> findByUserId(String userId);

  Optional<GoalEntity> findByUidAndUserId(String uid, String userId);

  List<GoalEntity> findByUserIdAndGoalDuration(String userId, LocalDate today);

  Optional<GoalEntity> findLastGoalByUserId(String userId);
}
