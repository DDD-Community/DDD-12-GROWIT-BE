package com.growit.app.retrospect.domain.retrospect.command;

public record CreateRetrospectCommand(
    String goalId, String planId, String userId, String content) {}
