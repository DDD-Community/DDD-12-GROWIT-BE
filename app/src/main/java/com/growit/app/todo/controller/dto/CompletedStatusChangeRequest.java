package com.growit.app.todo.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompletedStatusChangeRequest {
  @JsonProperty("isCompleted")
  private boolean completed;
}
