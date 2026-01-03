package com.growit.app.todo.controller.dto.response;

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

  @Getter
  @Builder
  @AllArgsConstructor
  public static class GoalTodoCount {
    private String id;
    private int todoCount;
  }
}
