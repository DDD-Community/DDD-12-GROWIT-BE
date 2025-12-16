package com.growit.app.todo.domain.dto;

import java.time.LocalDate;
import java.util.Optional;

public record GetCountByDateQueryFilter(
    LocalDate date, String userId, String goalId, Optional<String> toDoId) {}
