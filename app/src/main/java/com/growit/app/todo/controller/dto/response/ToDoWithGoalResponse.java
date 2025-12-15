package com.growit.app.todo.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growit.app.todo.domain.vo.Routine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ToDoWithGoalResponse {
  private ToDoInfo todo;
  private GoalInfo goal;

  @Getter
  @Builder
  @AllArgsConstructor
  public static class ToDoInfo {
    private String id;
    private String goalId;
    private String date;
    private String content;
    
    @JsonProperty("isImportant")
    private boolean important;
    
    @JsonProperty("isCompleted")
    private boolean completed;
    
    private Routine routine;
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class GoalInfo {
    private String id;
    private String name;
  }
}