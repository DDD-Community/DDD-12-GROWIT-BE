package com.growit.app.common.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BaseErrorResponse {
  private String message;
}
