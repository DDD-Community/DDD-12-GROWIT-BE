package com.growit.app.todo.domain.dto;

public record CompletedStatusChangeCommand(String id, String userId, boolean completed) {}
