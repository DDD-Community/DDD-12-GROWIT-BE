package com.growit.app.user.domain.user.dto;

public record OAuthCommand(String email, String provider, String providerId, String refreshToken) {
  public OAuthCommand(String email, String provider, String providerId) {
    this(email, provider, providerId, null);
  }
}
