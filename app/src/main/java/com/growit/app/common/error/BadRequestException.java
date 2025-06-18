package com.growit.app.common.error;

public class BadRequestException extends BaseException {
  public BadRequestException(String message) {
    super(message);
  }
}
