package com.growit.app.todos.domain.dto;

import java.time.LocalDate;

public record UpdateToDoCommand(
    String id, String userId, String planId, String content, LocalDate date) {}
