package com.growit.app.todo.domain.dto;

public record CompletedStatusChangeCommand(
    String id, String userId, Boolean completed, Boolean important) {}
