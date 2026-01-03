package com.growit.app.goal.domain.goal.dto;

import com.growit.app.goal.domain.goal.vo.GoalDuration;

public record CreateGoalCommand(String userId, String name, GoalDuration duration) {}
