package com.growit.goal.domain.goal.dto;

import com.growit.goal.domain.goal.vo.BeforeAfter;
import com.growit.goal.domain.goal.vo.GoalDuration;
import java.util.List;

public record CreateGoalCommand(
    String userId,
    String name,
    GoalDuration duration,
    BeforeAfter beforeAfter,
    List<PlanDto> plans) {}
