package com.growit.app.user.domain.token.service.error;

import com.growit.app.common.error.BaseException;

public class ExpiredTokenException extends BaseException {
  public ExpiredTokenException() {
    super("token.");
  }
}
