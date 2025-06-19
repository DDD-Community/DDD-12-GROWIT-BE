package com.growit.app.user.domain.user.vo;

import com.growit.app.common.exception.BaseException;
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
      throw new BaseException("");
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
