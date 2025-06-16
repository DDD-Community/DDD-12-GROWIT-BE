package com.growit.app.user.domain.user.dto;

import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;

public record SignUpCommand(
  Email email,
  String password,
  String name,
  String jobRoleId,
  CareerYear careerYear
) {
  public SignUpCommand withEncodedPassword(String encodedPassword) {
    return new SignUpCommand(email, encodedPassword, name, jobRoleId, careerYear);
  }
}
