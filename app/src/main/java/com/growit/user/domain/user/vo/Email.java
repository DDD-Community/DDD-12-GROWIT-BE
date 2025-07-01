package com.growit.user.domain.user.vo;

import com.growit.common.exception.BadRequestException;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record Email(String value) {

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

  public Email {
    if (!EMAIL_PATTERN.matcher(value).matches()) {
      throw new BadRequestException("유효하지 않은 이메일입니다.");
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
