package com.growit.app.todo.domain.dto;

import java.time.LocalDate;

public record GetDateQueryFilter(String userId, LocalDate date) {}
