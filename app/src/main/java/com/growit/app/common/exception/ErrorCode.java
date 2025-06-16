package com.growit.app.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  INVALID_INPUT(400, "입력한 정보가 올바르지 않습니다."),
  DUPLICATED_EMAIL(409, "해당 이메일로 이미 가입된 계정이 있습니다.");

  private final int status;
  private final String message;

}
