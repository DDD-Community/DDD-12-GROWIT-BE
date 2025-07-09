package com.growit.app.user.domain.token.service.exception;

import static com.growit.app.common.util.message.ErrorCode.USER_TOKEN_INVALID;

import com.growit.app.common.exception.BaseException;

public class InvalidTokenException extends BaseException {
  public InvalidTokenException() {
    super(USER_TOKEN_INVALID.getCode());
  }
}
