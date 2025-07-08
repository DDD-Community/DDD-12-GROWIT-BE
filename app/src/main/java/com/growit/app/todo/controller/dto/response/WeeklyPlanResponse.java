package com.growit.app.todo.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growit.app.todo.domain.ToDo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeeklyPlanResponse {
  private String id;
  private String goalId;
  private String planId;
  private String date;
  private String content;

  @JsonProperty("isCompleted")
  private boolean completed;

  public static WeeklyPlanResponse from(ToDo todo) {
    return WeeklyPlanResponse.builder()
        .id(todo.getId())
        .goalId(todo.getGoalId())
        .planId(todo.getPlanId())
        .date(todo.getDate().toString())
        .content(todo.getContent())
        .completed(todo.isCompleted())
        .build();
  }
}
