package com.growit.app.auth.domain.dto;

import com.growit.app.user.domain.entity.CareerYear;
import com.growit.app.user.domain.entity.JobRoleEntity;
import com.growit.app.user.domain.vo.Email;

public record SignUpCommand(
  Email email,
  String password,
  String name,
  JobRoleEntity jobRole,
  CareerYear careerYear
) {
}
