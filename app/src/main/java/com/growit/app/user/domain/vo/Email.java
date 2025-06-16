package com.growit.app.user.domain.vo;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public record Email(String value) {

  private static final Pattern EMAIL_PATTERN =
    Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

  public Email {
    Objects.requireNonNull(value, "이메일은 null 일 수 없습니다.");
    if (!EMAIL_PATTERN.matcher(value).matches()) {
      throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다: " + value);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
