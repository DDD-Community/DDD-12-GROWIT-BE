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
  @NotBlank(message = "{validation.signup.email.required}")
  @Email(message = "{validation.signup.email.invalid}")
  private String email;

  @NotBlank(message = "{validation.signup.password.required}")
  @Size(min = 8, message = "{validation.signup.password.size}")
  private String password;

  @NotBlank(message = "{validation.signup.name.required}")
  @Size(min = 2, message = "{validation.signup.name.size}")
  private String name;

  @NotBlank(message = "{validation.signup.job-role.required}")
  private String jobRoleId;

  @NotNull(message = "{validation.signup.career.required}")
  private String careerYear;

  @NotNull(message = "{validation.signup.consent.required}")
  private RequiredConsentRequest requiredConsent;
}
