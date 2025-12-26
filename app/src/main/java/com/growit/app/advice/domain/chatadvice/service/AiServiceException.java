package com.growit.app.advice.domain.chatadvice.service;

public class AiServiceException extends RuntimeException {
  public AiServiceException(String message) {
    super(message);
  }

  public AiServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
