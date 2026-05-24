package com.growit.app.todo.domain.dto;

import com.growit.app.todo.domain.TodoCategory;

public record CompletedStatusChangeCommand(
    String id, String userId, Boolean completed, TodoCategory category) {}
