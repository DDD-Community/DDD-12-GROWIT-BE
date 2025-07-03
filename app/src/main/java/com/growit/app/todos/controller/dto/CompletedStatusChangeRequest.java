package com.growit.app.todos.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompletedStatusChangeRequest {
  @NotBlank(message = "할일 상태값은 필수입니다.")
  private boolean isCompleted;
}
