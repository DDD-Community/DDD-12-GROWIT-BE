package com.growit.app.user.domain.token.service.exception;

import static com.growit.app.common.util.message.ErrorCode.USER_TOKEN_NOT_FOUND;

import com.growit.app.common.exception.BaseException;

public class TokenNotFoundException extends BaseException {
  public TokenNotFoundException() {
    super(USER_TOKEN_NOT_FOUND.getCode());
  }
}
