package com.growit.app.auth.controller.dto;

import com.growit.app.user.domain.vo.CareerYear;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import java.util.UUID;

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
  private UUID jobRoleId;
  @NotBlank
  private String careerYear;
}
