package com.growit.app.todo.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

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