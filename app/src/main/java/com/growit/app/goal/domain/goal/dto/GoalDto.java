package com.growit.app.goal.domain.goal.dto;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.List;

public record GoalDto(
    String id, String name, GoalDuration duration, BeforeAfter beforeAfter, List<Plan> plans) {
  public static GoalDto from(Goal goal) {
    return new GoalDto(
        goal.getId(),
        goal.getName(),
        goal.getDuration(),
        goal.getBeforeAfter(),
        goal.getPlans().stream()
            .map(plans -> new Plan(plans.getId(), plans.getContent()))
            .toList());
  }
}
