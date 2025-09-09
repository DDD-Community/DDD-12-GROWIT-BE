package com.growit.app.user.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthDto {
  @NotBlank(message = "{validation.signin.provider.required}")
  private String provider;
  @NotBlank(message = "{validation.signin.provider-id.required}")
  private String providerId;
}
