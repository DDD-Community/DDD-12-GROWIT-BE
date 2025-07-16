package com.growit.app.todo.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ToDoResponse {
  private String id;
  private PlanInfo plan;

  @Getter
  @AllArgsConstructor
  public static class PlanInfo {
    private String id;
    private int weekOfMonth;
  }
}
