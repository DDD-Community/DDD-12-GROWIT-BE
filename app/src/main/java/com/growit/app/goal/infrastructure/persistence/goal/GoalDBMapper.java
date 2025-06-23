package com.growit.app.goal.infrastructure.persistence.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.PlanEntity;
import org.springframework.stereotype.Component;

@Component
public class GoalDBMapper {
  public GoalEntity toEntity(Goal goal) {
    if (goal == null) return null;
    return GoalEntity.builder()
        .uid(goal.getId())
        .name(goal.getName())
        .startDate(goal.getDuration().startDate())
        .asIs(goal.getBeforeAfter().asIs())
        .toBe(goal.getBeforeAfter().toBe())
        .plans(
            goal.getPlans().stream()
                .map(plan -> new PlanEntity(plan.getId(), plan.getContent()))
                .toList())
        .build();
  }

  public Goal toDomain(GoalEntity entity) {
    if (entity == null) return null;
    return Goal.builder()
        .id(entity.getUid())
        .name(entity.getName())
        .duration(new GoalDuration(entity.getStartDate(), entity.getEndDate()))
        .beforeAfter(new BeforeAfter(entity.getAsIs(), entity.getToBe()))
        .plans(
            entity.getPlans().stream()
                .map(
                    planEntity ->
                        Plan.builder()
                            .id(planEntity.getUid())
                            .content(planEntity.getContent())
                            .build())
                .toList())
        .build();
  }
}
