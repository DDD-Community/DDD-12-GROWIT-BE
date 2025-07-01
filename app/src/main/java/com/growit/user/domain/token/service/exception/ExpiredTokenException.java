package com.growit.user.domain.token.service.exception;

import com.growit.common.exception.BaseException;

public class ExpiredTokenException extends BaseException {
  public ExpiredTokenException() {
    super("토큰정보가 만료되었습니다.");
  }
}
