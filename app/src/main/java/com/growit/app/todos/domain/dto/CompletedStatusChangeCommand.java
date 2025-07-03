package com.growit.app.todos.domain.dto;

public record CompletedStatusChangeCommand(String id, String userId, boolean completed) {}
