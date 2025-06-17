package com.growit.app.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseError extends RuntimeException {
  private final String message;
}
