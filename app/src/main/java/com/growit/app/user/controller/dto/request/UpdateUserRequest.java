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

  @NotBlank(message = "{validation.user.name.required}")
  @Size(min = 2, message = "{validation.user.name.size}")
  private String name;

  @NotBlank(message = "{validation.user.job-role.required}")
  private String jobRoleId;

  @NotNull(message = "{validation.user.career.required}")
  private CareerYear careerYear;
}
