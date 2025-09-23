package com.growit.app.retrospect.controller.retrospect.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRetrospectRequest {
  @Valid private KPTDto kpt;
}
