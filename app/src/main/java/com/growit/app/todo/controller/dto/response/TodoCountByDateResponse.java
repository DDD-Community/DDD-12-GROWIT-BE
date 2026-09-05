package com.growit.app.todo.controller.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.growit.app.todo.domain.vo.ToDoCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TodoCountByDateResponse {
  private String date;
  private List<GoalTodoCount> goals;
  @JsonInclude(JsonInclude.Include.NON_NULL)
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
    private ToDoCategory category;
    private int todoCount;
    private int completedCount;
  }
}
