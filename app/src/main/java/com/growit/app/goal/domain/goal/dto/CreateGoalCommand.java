package com.growit.app.goal.domain.goal.dto;

import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.util.List;

public record CreateGoalCommand(
    String userId, String name, GoalDuration duration, String toBe, List<PlanDto> plans) {}
