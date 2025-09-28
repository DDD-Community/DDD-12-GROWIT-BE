package com.growit.app.retrospect.controller.retrospect.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRetrospectRequest {
  @Valid @NotNull private KPTDto kpt;

  private String content;
}
