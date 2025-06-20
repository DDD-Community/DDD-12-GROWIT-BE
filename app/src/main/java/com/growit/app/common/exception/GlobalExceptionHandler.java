package com.growit.app.common.exception;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.growit.app.common.response.BaseErrorResponse;
import com.growit.app.user.domain.user.service.AlreadyExistEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.MalformedParametersException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
    BadRequestException.class,
    MalformedParametersException.class,
    ValueInstantiationException.class,
    IllegalStateException.class,
  })
  public ResponseEntity<BaseErrorResponse> returnBadRequestException(RuntimeException e) {
    return ResponseEntity.badRequest()
      .body(
        BaseErrorResponse.builder()
          .message("입력한 정보가 올바르지 않습니다. \n" + e.getMessage())
          .build());
  }

  @ExceptionHandler({
    AlreadyExistEmailException.class,
  })
  public ResponseEntity<BaseErrorResponse> handleConflictException(BaseException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(BaseErrorResponse.builder().message(e.getMessage()).build());
  }

  @ExceptionHandler({
    MethodArgumentNotValidException.class,
    IllegalArgumentException.class,
  })
  public ResponseEntity<BaseErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
    String errorMessages = e.getBindingResult().getFieldErrors().stream()
      .map(fieldError -> String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
      .reduce((m1, m2) -> m1 + "\n" + m2) // 여러 개면 줄바꿈
      .orElse("입력값이 올바르지 않습니다.");

    System.out.println("message ::" + e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(BaseErrorResponse.builder().message(e.getMessage()).build());
  }

}
