package com.growit.app.user.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpKaKaoRequest {
  @NotBlank(message = "{validation.user.name.required}")
  @Size(min = 2, message = "{validation.user.name.size}")
  private String name;

  @Valid
  @NotNull(message = "{validation.signup.consent.required}")
  private RequiredConsentRequest requiredConsent;

  @NotNull private String registrationToken;

  @Deprecated private String jobRoleId;

  @Deprecated private String careerYear;
}
