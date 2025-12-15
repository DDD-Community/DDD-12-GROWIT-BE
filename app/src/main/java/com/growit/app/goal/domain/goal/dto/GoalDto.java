package com.growit.app.goal.domain.goal.dto;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.Collections;

public record GoalDto(
    String id,
    String userId,
    String name,
    GoalDuration goalDuration,
    String toBe) {
  public static GoalDto toDto(Goal goal) {
    return new GoalDto(
        goal.getId(),
        goal.getUserId(),
        goal.getName(),
        goal.getDuration(),
        goal.getName()); // getToBe() 메서드가 제거됨, 목표명으로 대체
  }
}
