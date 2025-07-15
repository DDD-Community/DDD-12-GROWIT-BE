package com.growit.app.user.controller.dto.request;

import com.growit.app.user.domain.user.vo.CareerYear;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserRequest {

  @NotBlank(message = "{validation.signup.name.required}")
  @Size(min = 2, message = "{validation.signup.name.size}")
  private String name;

  @NotBlank(message = "{validation.signup.job-role.required}")
  private String jobRoleId;

  @NotNull(message = "{validation.signup.career.required}")
  private CareerYear careerYear;
}
