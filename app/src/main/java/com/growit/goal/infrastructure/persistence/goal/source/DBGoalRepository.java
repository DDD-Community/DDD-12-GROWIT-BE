package com.growit.goal.infrastructure.persistence.goal.source;

import com.growit.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBGoalRepository extends JpaRepository<GoalEntity, Long>, DBGoalQueryRepository {
  Optional<GoalEntity> findByUid(String uid);
}
