package com.growit.app.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.util.UUID;

@Getter
public class SignUpRequest {
  @NotBlank
  private String email;
  @NotBlank
  private String password;
  @NotBlank
  private String name;
  @NotNull
  private UUID jobRoleId;
  @NotBlank
  private String careerYear;
}
