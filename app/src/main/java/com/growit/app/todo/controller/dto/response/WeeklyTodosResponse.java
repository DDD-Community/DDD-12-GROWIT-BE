package com.growit.app.todo.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.TodoCategory;
import com.growit.app.todo.domain.vo.Routine;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeeklyTodosResponse {
  private String id;
  private String goalId;
  private String date;
  private String content;

  @JsonProperty("isCompleted")
  private boolean completed;

  private TodoCategory category;

  private Routine routine;

  public static WeeklyTodosResponse from(ToDo todo) {
    return WeeklyTodosResponse.builder()
        .id(todo.getId())
        .goalId(todo.getGoalId())
        .date(todo.getDate().toString())
        .content(todo.getContent())
        .completed(todo.isCompleted())
        .category(todo.getCategory())
        .routine(todo.getRoutine())
        .build();
  }
}
