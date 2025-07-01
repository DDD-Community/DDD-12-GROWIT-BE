package com.growit.user.domain.token.service.exception;

import com.growit.common.exception.BaseException;

public class TokenNotFoundException extends BaseException {
  public TokenNotFoundException() {
    super("토큰정보가 존재하지 않습니다.");
  }
}
