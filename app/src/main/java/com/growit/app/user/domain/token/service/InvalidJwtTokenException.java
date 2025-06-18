package com.growit.app.user.domain.token.service;

import com.growit.app.common.error.BaseException;

public class InvalidJwtTokenException extends BaseException {
  public InvalidJwtTokenException() {
    super("Invalid token.");
  }
}
