package com.growit.app.goal.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePlanRequest {
  @NotNull(message = "{validation.goal.plan.content.required}")
  @Valid
  private String content;
}
