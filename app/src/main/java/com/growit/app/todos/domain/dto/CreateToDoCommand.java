package com.growit.app.todos.domain.dto;

import java.time.LocalDate;

public record CreateToDoCommand(
    String userId, String goalId, String planId, String content, LocalDate date) {}
