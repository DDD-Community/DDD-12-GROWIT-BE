package com.growit.app.user.domain.user.service;

import com.growit.app.common.error.BaseException;

public class AlreadyExistsEmailException extends BaseException {
  public AlreadyExistsEmailException() {
    super("해당 이메일로 이미 가입된 계정이 있습니다.");
  }
}
