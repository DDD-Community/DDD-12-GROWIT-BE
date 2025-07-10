package com.growit.app.retrospect.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRetrospectRequest {
  @NotBlank(message = "{validation.retrospect.content.required}")
  @Size(min = 10, max = 200, message = "{validation.retrospect.content.size}")
  private String content;
}
