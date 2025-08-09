package com.growit.app.goal.infrastructure.persistence.goalretrospect.source;

import com.growit.app.goal.infrastructure.persistence.goalretrospect.source.entity.GoalRetrospectEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBGoalRetrospectRepository extends JpaRepository<GoalRetrospectEntity, Long> {
  Optional<GoalRetrospectEntity> findByUid(String uid);

  Optional<GoalRetrospectEntity> findByGoalId(String goalId);

  List<GoalRetrospectEntity> findAllByGoalId(String goalId);
}