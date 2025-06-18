package com.growit.app.user.domain.token.service.error;

import com.growit.app.common.error.BaseException;

public class ExpiredTokenException extends BaseException {
  public ExpiredTokenException() {
    super("토큰정보가 만료되었습니다.");
  }
}
