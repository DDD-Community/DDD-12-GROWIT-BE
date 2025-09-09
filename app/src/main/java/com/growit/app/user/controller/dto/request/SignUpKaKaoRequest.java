package com.growit.app.user.controller.dto.request;

import com.growit.app.user.domain.user.vo.CareerYear;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpKaKaoRequest {
  @NotBlank(message = "{validation.signup.email.required}")
  @Email(message = "{validation.signup.email.invalid}")
  private String email;

  @NotBlank(message = "{validation.user.name.required}")
  @Size(min = 2, message = "{validation.user.name.size}")
  private String name;

  @NotBlank(message = "{validation.user.job-role.required}")
  private String jobRoleId;

  @NotNull(message = "{validation.user.career.required}")
  private CareerYear careerYear;

  @NotNull(message = "{validation.signup.consent.required}")
  private RequiredConsentRequest requiredConsent;

  @NotNull
  private OAuthDto oauth;
}
