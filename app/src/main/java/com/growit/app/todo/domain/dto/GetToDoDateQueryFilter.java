package com.growit.app.todo.domain.dto;

import java.time.LocalDate;

public record GetToDoDateQueryFilter(String userId, String goalId, LocalDate date) {}
