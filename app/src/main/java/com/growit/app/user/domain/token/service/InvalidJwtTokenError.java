package com.growit.app.user.domain.token.service;

import com.growit.app.common.error.BaseError;

public class InvalidJwtTokenError extends BaseError {
  public InvalidJwtTokenError() {
    super("Invalid token.");
  }
}
