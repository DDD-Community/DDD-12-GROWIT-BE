package com.growit.goal.infrastructure.persistence.goal;

import com.growit.goal.domain.goal.Goal;
import com.growit.goal.domain.goal.plan.Plan;
import com.growit.goal.domain.goal.vo.BeforeAfter;
import com.growit.goal.domain.goal.vo.GoalDuration;
import com.growit.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import com.growit.goal.infrastructure.persistence.goal.source.entity.PlanEntity;
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
            .asIs(goal.getBeforeAfter().asIs())
            .toBe(goal.getBeforeAfter().toBe())
            .build();
    entity.setPlans(
        goal.getPlans().stream()
            .map(
                plan ->
                    new PlanEntity(plan.getId(), plan.getWeekOfMonth(), plan.getContent(), entity))
            .toList());
    entity.setDeletedAt(goal.getDeleted() ? LocalDateTime.now() : null);
    return entity;
  }

  public Goal toDomain(GoalEntity entity) {
    if (entity == null) return null;
    return Goal.builder()
        .id(entity.getUid())
        .userId(entity.getUserId())
        .name(entity.getName())
        .duration(new GoalDuration(entity.getStartDate(), entity.getEndDate()))
        .beforeAfter(new BeforeAfter(entity.getAsIs(), entity.getToBe()))
        .plans(
            entity.getPlans().stream()
                .map(
                    planEntity ->
                        Plan.builder()
                            .id(planEntity.getUid())
                            .weekOfMonth(planEntity.getWeekOfMonth())
                            .content(planEntity.getContent())
                            .build())
                .toList())
        .isDelete(entity.getDeletedAt() != null)
        .build();
  }
}
