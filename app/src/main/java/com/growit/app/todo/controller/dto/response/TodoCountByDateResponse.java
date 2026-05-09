package com.growit.app.todo.controller.dto.response;

import com.growit.app.todo.domain.TodoCategory;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TodoCountByDateResponse {
  private String date;
  private List<GoalTodoCount> goals;
  private List<CategoryTodoCount> categories;

  @Getter
  @Builder
  @AllArgsConstructor
  public static class GoalTodoCount {
    private String id;
    private int todoCount;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class CategoryTodoCount {
    private TodoCategory category;
    private int todoCount;
    private int completedCount;
  }
}
