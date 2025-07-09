package com.growit.app.user.domain.token.service.exception;

import static com.growit.app.common.util.message.ErrorCode.USER_TOKEN_EXPIRED;

import com.growit.app.common.exception.BaseException;

public class ExpiredTokenException extends BaseException {
  public ExpiredTokenException() {
    super(USER_TOKEN_EXPIRED.getCode());
  }
}
