package com.growit.app.goal.domain.goal.retrospect.dto;

public record CreateRetrospectCommand(
    String goalId,
    String planId,
    String userId,
    String content) {}