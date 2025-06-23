package com.growit.app.goal.infrastructure.persistence.goal.source;

import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBGoalRepository extends JpaRepository<GoalEntity, Long> {
  Optional<GoalEntity> findByuId(String uid);
}
