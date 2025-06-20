package com.growit.app.user.domain.token.service.exception;

import com.growit.app.common.exception.BaseException;

public class InvalidTokenException extends BaseException {
  public InvalidTokenException() {
    super("토큰정보가 올바르지 않습니다.");
  }
}
