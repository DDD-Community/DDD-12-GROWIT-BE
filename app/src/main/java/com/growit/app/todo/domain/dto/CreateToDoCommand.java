package com.growit.app.todo.domain.dto;

import java.time.LocalDate;

public record CreateToDoCommand(
    String userId, String goalId, String planId, String content, LocalDate date) {}
