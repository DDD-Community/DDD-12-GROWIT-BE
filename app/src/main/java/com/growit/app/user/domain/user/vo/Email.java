package com.growit.app.user.domain.user.vo;

public record Email(String value) {
  public Email {
    if (value == null || !value.contains("@")) {
      throw new IllegalArgumentException("유효하지 않은 이메일입니다.");
    }
  }
}
