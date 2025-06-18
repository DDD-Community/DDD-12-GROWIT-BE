package com.growit.app.user.domain.user.vo;

import com.growit.app.common.error.BadRequestException;

public record Email(String value) {
  public Email {
    if (value == null || !value.contains("@")) {
      throw new BadRequestException("유효하지 않은 이메일입니다.");
    }
  }
}
