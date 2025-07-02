package com.growit.app.retrospect.domain.retrospect.command;

public record UpdateRetrospectCommand(
    String id, String userId, String content) {}
