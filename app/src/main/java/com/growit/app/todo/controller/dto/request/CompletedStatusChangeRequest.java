package com.growit.app.todo.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CompletedStatusChangeRequest {
  @JsonProperty("isCompleted")
  private Boolean completed;

  @JsonProperty("isImportant")
  private Boolean important; // Use Boolean wrapper to allow null values
}
