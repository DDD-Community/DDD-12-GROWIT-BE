package com.growit.app.common.exception;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.growit.app.common.response.BaseErrorResponse;
import com.growit.app.user.domain.token.service.exception.ExpiredTokenException;
import com.growit.app.user.domain.token.service.exception.InvalidTokenException;
import com.growit.app.user.domain.token.service.exception.TokenNotFoundException;
import com.growit.app.user.domain.user.service.AlreadyExistEmailException;

import java.lang.reflect.MalformedParametersException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
    BadRequestException.class,
    MalformedParametersException.class,
    IllegalArgumentException.class,
    ValueInstantiationException.class,
  })
  public ResponseEntity<BaseErrorResponse> returnBadRequestException(RuntimeException e) {
    return ResponseEntity.badRequest()
      .body(
        BaseErrorResponse.builder()
          .message("입력한 정보가 올바르지 않습니다. \n" + e.getMessage())
          .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseErrorResponse> handleException(Exception e) {
    return ResponseEntity.internalServerError()
      .body(BaseErrorResponse.builder().message(e.getMessage()).build());
  }

  @ExceptionHandler({
    TokenNotFoundException.class,
    InvalidTokenException.class,
  })
  public ResponseEntity<BaseErrorResponse> handleForbiddenException(BaseException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
      .body(BaseErrorResponse.builder().message(e.getMessage()).build());
  }

  @ExceptionHandler({
    ExpiredTokenException.class,
  })
  public ResponseEntity<BaseErrorResponse> handleUnauthorizedException(BaseException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
      .body(BaseErrorResponse.builder().message(e.getMessage()).build());
  }

  @ExceptionHandler({
    AlreadyExistEmailException.class,
  })
  public ResponseEntity<BaseErrorResponse> handleConflictException(BaseException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(BaseErrorResponse.builder().message(e.getMessage()).build());
  }

  @ExceptionHandler({
    NotFoundException.class,
  })
  public ResponseEntity<BaseErrorResponse> handleNotFoundException(BaseException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(BaseErrorResponse.builder().message(e.getMessage()).build());
  }
}
