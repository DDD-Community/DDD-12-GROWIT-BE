package com.growit.app.user.infrastructure.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AppleTokenResponse {
  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("expires_in")
  private Integer expiresIn;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("id_token")
  private String idToken;
}
