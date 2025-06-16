package com.growit.app.user.domain.dto;

import com.growit.app.user.domain.vo.CareerYear;
import com.growit.app.user.domain.vo.Email;

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
