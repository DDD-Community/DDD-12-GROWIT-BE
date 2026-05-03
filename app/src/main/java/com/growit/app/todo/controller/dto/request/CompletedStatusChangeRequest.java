package com.growit.app.todo.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growit.app.todo.domain.TodoCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompletedStatusChangeRequest {
  @JsonProperty("isCompleted")
  private Boolean completed;

  private TodoCategory category; // nullable
}
