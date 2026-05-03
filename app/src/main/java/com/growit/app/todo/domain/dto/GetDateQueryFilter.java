package com.growit.app.todo.domain.dto;

import com.growit.app.todo.domain.TodoCategory;
import java.time.LocalDate;

public record GetDateQueryFilter(String userId, LocalDate date, TodoCategory category) {
  public GetDateQueryFilter(String userId, LocalDate date) {
    this(userId, date, null);
  }
}
