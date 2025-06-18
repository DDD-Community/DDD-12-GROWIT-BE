package com.growit.app.common.exception;

import com.growit.app.common.response.BaseErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseErrorResponse> handleException(RuntimeException e) {
    return ResponseEntity.internalServerError()
      .body(BaseErrorResponse.builder().message(e.getMessage()).build());
  }
}
