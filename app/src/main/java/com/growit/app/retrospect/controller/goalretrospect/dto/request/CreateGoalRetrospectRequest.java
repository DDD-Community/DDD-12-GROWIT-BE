package com.growit.app.retrospect.controller.goalretrospect.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateGoalRetrospectRequest {
  @NotBlank(message = "{validation.goal-retrospect.goal-id.required}")
  private String goalId;
}
