package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.PlanetEntity;
import org.springframework.stereotype.Component;

@Component
public class GoalDBMapper {

  public GoalEntity toEntity(Goal goal, String userId, PlanetEntity planetEntity) {
    if (goal == null) return null;

    return GoalEntity.fromDomain(goal, userId, planetEntity);
  }

  public Goal toDomain(GoalEntity entity) {
    if (entity == null) return null;

    return entity.toDomain();
  }

  public void updateEntity(GoalEntity entity, Goal goal) {
    if (entity == null || goal == null) return;

    entity.updateFromDomain(goal);
  }
}
