package com.growit.app.user.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignInAppleRequest {

  @NotBlank(message = "idToken은 필수입니다.")
  private String idToken;

  @NotBlank(message = "authorizationCode는 필수입니다.")
  private String authorizationCode;
}
