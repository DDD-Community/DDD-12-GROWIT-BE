package com.growit.app.user.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpRequest {
  @NotBlank
  private String email;
  @NotBlank
  @Size(min=8)
  private String password;
  @NotBlank
  @Size(min = 2)
  private String name;
  @NotNull
  private String jobRoleId;
  @NotBlank
  private String careerYear;
}
