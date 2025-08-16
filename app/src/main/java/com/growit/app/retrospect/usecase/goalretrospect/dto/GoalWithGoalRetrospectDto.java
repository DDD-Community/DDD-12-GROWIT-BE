package com.growit.app.retrospect.usecase.goalretrospect.dto;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import jakarta.annotation.Nullable;

public record GoalWithGoalRetrospectDto(Goal goal, @Nullable GoalRetrospect goalRetrospect) {}
