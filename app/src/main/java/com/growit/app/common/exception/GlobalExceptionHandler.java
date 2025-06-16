package com.growit.app.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public record SimpleErrorResponse(int status, String message) {
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<SimpleErrorResponse> handleCustomException(CustomException ex) {
    return ResponseEntity
      .status(ex.status())
      .body(new SimpleErrorResponse(ex.status(), ex.message()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<SimpleErrorResponse> handleOther(Exception ex) {
    // 알 수 없는 에러는 500으로 처리
    return ResponseEntity
      .status(500)
      .body(new SimpleErrorResponse(500, "서버 오류가 발생했습니다."));
  }
}
