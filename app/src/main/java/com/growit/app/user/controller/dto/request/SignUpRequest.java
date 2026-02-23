package com.growit.app.user.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest {
  @NotBlank(message = "{validation.signup.email.required}")
  @Email(message = "{validation.signup.email.invalid}")
  private String email;

  @NotBlank(message = "{validation.signup.password.required}")
  @Size(min = 8, message = "{validation.signup.password.size}")
  private String password;

  @NotBlank(message = "{validation.user.name.required}")
  @Size(min = 2, message = "{validation.user.name.size}")
  private String name;

  private String lastName;

  @NotNull(message = "{validation.signup.consent.required}")
  private RequiredConsentRequest requiredConsent;

  @Deprecated private String jobRoleId;

  @Deprecated private String careerYear;
}
