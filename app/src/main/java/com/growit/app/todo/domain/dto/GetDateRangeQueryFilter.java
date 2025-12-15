package com.growit.app.todo.domain.dto;

import java.time.LocalDate;

public record GetDateRangeQueryFilter(String userId, LocalDate fromDate, LocalDate toDate) {}