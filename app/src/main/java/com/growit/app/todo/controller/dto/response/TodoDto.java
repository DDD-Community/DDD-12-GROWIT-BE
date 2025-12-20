package com.growit.app.todo.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TodoDto {
  private String id;
  private String goalId;
  private String date;
  private String content;

  @JsonProperty("isImportant")
  private boolean important;

  @JsonProperty("isCompleted")
  private boolean completed;

  private RoutineDto routine;
}
