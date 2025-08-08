package com.growit.app.goal.domain.goal.dto;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.List;

public record GoalDto(
    String id,
    String userId,
    String name,
    GoalDuration goalDuration,
    String toBe,
    List<Plan> planList) {
  public static GoalDto toDto(Goal goal) {
    return new GoalDto(
        goal.getId(),
        goal.getUserId(),
        goal.getName(),
        goal.getDuration(),
        goal.getToBe(),
        goal.getPlans());
  }
}
