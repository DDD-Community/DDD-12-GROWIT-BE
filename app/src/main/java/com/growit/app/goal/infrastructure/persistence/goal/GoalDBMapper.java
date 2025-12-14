package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.goal.domain.goal.vo.Planet;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class GoalDBMapper {
  public GoalEntity toEntity(Goal goal) {
    if (goal == null) return null;
    GoalEntity entity =
        GoalEntity.builder()
            .uid(goal.getId())
            .userId(goal.getUserId())
            .name(goal.getName())
            .startDate(goal.getDuration().startDate())
            .endDate(goal.getDuration().endDate())
            .toBe(goal.getToBe())
            .category(goal.getCategory())
            .updateStatus(goal.getUpdateStatus())
            .build();
    // Plans are no longer part of Goal domain
    entity.setDeletedAt(goal.getDeleted() ? LocalDateTime.now() : null);
    return entity;
  }

  public Goal toDomain(GoalEntity entity) {
    if (entity == null) return null;
    
    var planet = Planet.of("Earth", "/images/earth_done.png", "/images/earth_progress.png"); // Default planet
    
    var duration = new GoalDuration(entity.getStartDate(), entity.getEndDate());
    var goal = new Goal(
        entity.getUid(),
        entity.getName(),
        planet,
        duration
    );
    
    // Set status based on updateStatus
    if (entity.getUpdateStatus() != null) {
      goal.updateByGoalUpdateStatus(entity.getUpdateStatus());
    }
    
    // Set deleted status
    if (entity.getDeletedAt() != null) {
      goal.deleted();
    }
    
    return goal;
  }
}
