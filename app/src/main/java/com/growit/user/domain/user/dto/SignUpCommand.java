package com.growit.user.domain.user.dto;

import com.growit.user.domain.user.vo.CareerYear;
import com.growit.user.domain.user.vo.Email;

public record SignUpCommand(
    Email email, String password, String name, String jobRoleId, CareerYear careerYear) {
  public SignUpCommand encodePassword(String password) {
    return new SignUpCommand(email, password, name, jobRoleId, careerYear);
  }
}
