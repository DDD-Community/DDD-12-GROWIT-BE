package com.growit.app.common.error;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.growit.app.common.dto.GrowitErrorResponse;
import com.growit.app.user.domain.token.service.error.ExpiredTokenException;
import com.growit.app.user.domain.token.service.error.InvalidTokenException;
import com.growit.app.user.domain.token.service.error.TokenNotFoundException;
import com.growit.app.user.domain.user.service.AlreadyExistsEmailException;
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
  public ResponseEntity<GrowitErrorResponse> returnBadRequestException(RuntimeException e) {
    return ResponseEntity.badRequest()
        .body(
            GrowitErrorResponse.builder()
                .message("입력한 정보가 올바르지 않습니다. \n" + e.getMessage())
                .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<GrowitErrorResponse> handleException(Exception e) {
    return ResponseEntity.internalServerError()
        .body(GrowitErrorResponse.builder().message(e.getMessage()).build());
  }

  @ExceptionHandler({
    TokenNotFoundException.class,
    InvalidTokenException.class,
  })
  public ResponseEntity<GrowitErrorResponse> handleForbiddenException(BaseException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(GrowitErrorResponse.builder().message(e.getMessage()).build());
  }

  @ExceptionHandler({
    ExpiredTokenException.class,
  })
  public ResponseEntity<GrowitErrorResponse> handleUnauthorizedException(BaseException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(GrowitErrorResponse.builder().message(e.getMessage()).build());
  }

  @ExceptionHandler({
    AlreadyExistsEmailException.class,
  })
  public ResponseEntity<GrowitErrorResponse> handleConflictException(BaseException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(GrowitErrorResponse.builder().message(e.getMessage()).build());
  }

  @ExceptionHandler({
    NotFoundException.class,
  })
  public ResponseEntity<GrowitErrorResponse> handleNotFoundException(BaseException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(GrowitErrorResponse.builder().message(e.getMessage()).build());
  }
}
