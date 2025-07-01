package com.growit.common.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BaseErrorResponse {
  private String message;
}
