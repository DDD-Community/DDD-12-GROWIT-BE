package com.growit.app.user.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest {
  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "유효한 이메일 형식이어야 합니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
  private String password;

  @NotBlank(message = "이름은 필수입니다.")
  @Size(min = 2)
  private String name;

  @NotBlank(message = "직무는 필수입니다.")
  private String jobRoleId;

  @NotNull(message = "경력 연차는 필수입니다.")
  private String careerYear;

  @NotNull(message = "약관동의는 필수입니다.")
  private RequiredConsentRequest requiredConsent;
}
