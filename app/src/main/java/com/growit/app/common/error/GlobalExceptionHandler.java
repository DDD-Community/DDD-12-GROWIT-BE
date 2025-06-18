package com.growit.app.common.error;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.growit.app.common.dto.GrowitErrorResponse;
import java.lang.reflect.MalformedParametersException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
    BadRequestError.class,
    MalformedParametersException.class,
    IllegalArgumentException.class,
    ValueInstantiationException.class,
  })
  public ResponseEntity<GrowitErrorResponse> returnBadRequestException(RuntimeException e) {
    return ResponseEntity.badRequest()
        .body(GrowitErrorResponse.builder().message(e.getMessage()).build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<GrowitErrorResponse> handleMalformedParametersException(Exception e) {
    return ResponseEntity.internalServerError()
        .body(GrowitErrorResponse.builder().message(e.getMessage()).build());
  }
}
