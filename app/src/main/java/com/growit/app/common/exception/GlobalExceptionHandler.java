package com.growit.app.common.exception;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.growit.app.common.response.BaseErrorResponse;
import com.growit.app.common.util.message.MessageService;
import com.growit.app.user.domain.token.service.exception.ExpiredTokenException;
import com.growit.app.user.domain.token.service.exception.InvalidTokenException;
import com.growit.app.user.domain.token.service.exception.TokenNotFoundException;
import com.growit.app.user.domain.user.service.AlreadyExistEmailException;
import java.lang.reflect.MalformedParametersException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
  private final MessageService messageService;

  @ExceptionHandler({
    BadRequestException.class,
    MalformedParametersException.class,
    IllegalArgumentException.class,
    ValueInstantiationException.class,
  })
  public ResponseEntity<BaseErrorResponse> returnBadRequestException(RuntimeException e) {
    return ResponseEntity.badRequest()
        .body(BaseErrorResponse.builder().message(messageService.msg(e.getMessage())).build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseErrorResponse> handleException(Exception e) {
    return ResponseEntity.internalServerError()
        .body(BaseErrorResponse.builder().message(messageService.msg(e.getMessage())).build());
  }

  @ExceptionHandler({
    TokenNotFoundException.class,
    InvalidTokenException.class,
    ForbiddenException.class,
  })
  public ResponseEntity<BaseErrorResponse> handleForbiddenException(BaseException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(BaseErrorResponse.builder().message(messageService.msg(e.getMessage())).build());
  }

  @ExceptionHandler({
    ExpiredTokenException.class,
  })
  public ResponseEntity<BaseErrorResponse> handleUnauthorizedException(BaseException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(BaseErrorResponse.builder().message(messageService.msg(e.getMessage())).build());
  }

  @ExceptionHandler({
    AlreadyExistEmailException.class,
  })
  public ResponseEntity<BaseErrorResponse> handleConflictException(BaseException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(BaseErrorResponse.builder().message(messageService.msg(e.getMessage())).build());
  }

  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<BaseErrorResponse> handleNotFoundException(BaseException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(BaseErrorResponse.builder().message(messageService.msg(e.getMessage())).build());
  }

  @ExceptionHandler({NoResourceFoundException.class})
  public ResponseEntity<BaseErrorResponse> handleNoResourceFoundException(
      NoResourceFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(BaseErrorResponse.builder().message(messageService.msg(e.getMessage())).build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseErrorResponse> handleValidationError(
      MethodArgumentNotValidException ex) {
    List<String> messages = resolveValidationMessages(ex);
    String joinedMessage = String.join("\n", messages);
    return ResponseEntity.badRequest()
        .body(BaseErrorResponse.builder().message(joinedMessage).build());
  }

  private List<String> resolveValidationMessages(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getAllErrors().stream()
        .map(error -> messageService.msg(error.getDefaultMessage()))
        .toList();
  }
}
