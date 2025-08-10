package com.growit.app.goal.controller.goalretrospect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateGoalRetrospectRequest {
  @NotBlank(message = "{validation.goal-retrospect.content.required}")
  @Size(min = 10, max = 1000, message = "{validation.goal-retrospect.content.size}")
  private String content;
}