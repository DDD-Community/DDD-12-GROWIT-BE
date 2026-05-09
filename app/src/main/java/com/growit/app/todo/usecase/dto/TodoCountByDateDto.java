package com.growit.app.todo.usecase.dto;

import com.growit.app.todo.domain.TodoCategory;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoCountByDateDto {
  private final LocalDate date;
  private final List<GoalTodoCount> goalCounts;
  private final List<CategoryTodoCount> categoryCounts;

  @Getter
  @AllArgsConstructor
  public static class GoalTodoCount {
    private final String goalId;
    private final int todoCount;
  }

  @Getter
  @AllArgsConstructor
  public static class CategoryTodoCount {
    private final TodoCategory category;
    private final int todoCount;
    private final int completedCount;
  }
}
