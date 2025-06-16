package com.growit.app.common.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GrowitErrorResponse {
  private String message;
}
