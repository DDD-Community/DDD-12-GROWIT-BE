package com.growit.app.todo.usecase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class TodoCountByDateDto {
  private final LocalDate date;
  private final List<GoalTodoCount> goalCounts;

  @Getter
  @AllArgsConstructor
  public static class GoalTodoCount {
    private final String goalId;
    private final int todoCount;
  }
}