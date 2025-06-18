package com.growit.app.user.domain.token.service.error;

import com.growit.app.common.error.BaseException;

public class InvalidTokenException extends BaseException {
  public InvalidTokenException() {
    super("Invalid token.");
  }
}
