package com.growit.app.user.domain.user.vo;

import static com.growit.app.common.util.message.ErrorCode.USER_INVALID_EMAIL;

import com.growit.app.common.exception.BadRequestException;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record Email(String value) {

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

  public Email {
    if (!EMAIL_PATTERN.matcher(value).matches()) {
      throw new BadRequestException(USER_INVALID_EMAIL.getCode());
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
