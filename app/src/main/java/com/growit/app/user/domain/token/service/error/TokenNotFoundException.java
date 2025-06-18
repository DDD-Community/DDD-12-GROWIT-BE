package com.growit.app.user.domain.token.service.error;

import com.growit.app.common.error.BaseException;

public class TokenNotFoundException extends BaseException {
  public TokenNotFoundException() {
    super("토큰정보가 존재하지 않습니다.");
  }
}
