package com.growit.app.user.domain.user.service;

import static com.growit.app.common.util.message.ErrorCode.USER_ALREADY_REGISTERED;

import com.growit.app.common.exception.BaseException;

public class AlreadyExistEmailException extends BaseException {
  public AlreadyExistEmailException() {
    super(USER_ALREADY_REGISTERED.getCode());
  }
}
