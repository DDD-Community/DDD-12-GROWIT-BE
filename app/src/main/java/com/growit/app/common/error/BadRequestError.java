package com.growit.app.common.error;

public class BadRequestError extends RuntimeException {
  public BadRequestError(String message) {
    super(message);
  }
}
