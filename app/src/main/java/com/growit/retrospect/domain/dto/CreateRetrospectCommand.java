package com.growit.retrospect.domain.dto;

public record CreateRetrospectCommand(
    String goalId, String planId, String userId, String content) {}
