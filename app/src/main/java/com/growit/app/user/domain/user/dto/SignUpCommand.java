package com.growit.app.user.domain.user.dto;

import static com.growit.app.common.util.message.ErrorCode.USER_REQUIRED_INVALID;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.OAuth;

public record SignUpCommand(
    Email email,
    String password,
    String name,
    String jobRoleId,
    CareerYear careerYear,
    OAuth oAuth) {
  public SignUpCommand encodePassword(String password) {
    return new SignUpCommand(email, password, name, jobRoleId, careerYear, oAuth);
  }

  public void checkOAuth() {
    if (oAuth == null) {
      throw new BadRequestException(USER_REQUIRED_INVALID.getCode());
    }
  }
}
