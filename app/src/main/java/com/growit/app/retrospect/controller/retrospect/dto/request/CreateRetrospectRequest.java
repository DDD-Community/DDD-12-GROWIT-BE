package com.growit.app.retrospect.controller.retrospect.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateRetrospectRequest {
  @NotBlank(message = "{validation.retrospect.goal-id.required}")
  private String goalId;

  @NotBlank(message = "{validation.retrospect.plan-id.required}")
  private String planId;

  @NotBlank(message = "{validation.retrospect.content.required}")
  @Size(min = 10, max = 200, message = "{validation.retrospect.content.size}")
  private String content;
}
