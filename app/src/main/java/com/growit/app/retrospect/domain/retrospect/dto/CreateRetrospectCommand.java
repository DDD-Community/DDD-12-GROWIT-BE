package com.growit.app.retrospect.domain.retrospect.dto;

public record CreateRetrospectCommand(
    String goalId,
    String planId,
    String userId,
    String content) {}