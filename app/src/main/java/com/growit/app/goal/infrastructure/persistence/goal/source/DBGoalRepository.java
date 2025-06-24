package com.growit.app.goal.infrastructure.persistence.goal.source;

import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBGoalRepository extends JpaRepository<GoalEntity, Long>, DBGoalQueryRepository {
}
