package com.growit.app.todos.domain;

import java.time.LocalDate;

public record CreateToDoCommand(
    String userId, String goalId, String planId, String content, LocalDate date) {}
