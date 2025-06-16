package com.growit.app.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public int status() {
    return errorCode.getStatus();
  }

  public String message() {
    return errorCode.getMessage();
  }
}
