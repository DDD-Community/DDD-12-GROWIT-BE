package com.growit.app.retrospect.controller.retrospect.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRetrospectRequest {
  @NotBlank(message = "{validation.retrospect.goal-id.required}")
  private String goalId;

  @NotBlank(message = "{validation.retrospect.plan-id.required}")
  private String planId;

  @Valid private KPTDto kpt;
}
