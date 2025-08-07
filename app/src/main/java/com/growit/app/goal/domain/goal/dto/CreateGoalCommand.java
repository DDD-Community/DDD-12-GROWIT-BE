package com.growit.app.goal.domain.goal.dto;

import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalCategory;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.List;

public record CreateGoalCommand(
    String userId,
    String name,
    GoalDuration duration,
    BeforeAfter beforeAfter,
    GoalCategory category,
    List<PlanDto> plans) {}
