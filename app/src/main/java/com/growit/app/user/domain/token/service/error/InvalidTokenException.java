package com.growit.app.user.domain.token.service.error;

import com.growit.app.common.error.BaseException;

public class InvalidTokenException extends BaseException {
  public InvalidTokenException() {
    super("토큰정보가 올바르지 않습니다.");
  }
}
