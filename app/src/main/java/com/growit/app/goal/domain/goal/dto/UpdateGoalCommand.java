package com.growit.app.goal.domain.goal.dto;

import com.growit.app.goal.domain.goal.vo.GoalCategory;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.List;

public record UpdateGoalCommand(
    String id,
    String userId,
    String name,
    GoalDuration duration,
    String toBe,
    GoalCategory category,
    List<PlanDto> plans) {}
