package com.growit.app.user.domain.user.service;

import com.growit.app.common.exception.BaseException;

public class AlreadyExistEmailException extends BaseException {
  public AlreadyExistEmailException() {
    super("해당 이메일로 이미 가입된 계정이 있습니다.");
  }
}
