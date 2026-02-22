package com.growit.app.user.domain.user.vo;

public record OAuth(String provider, String providerId, String refreshToken) {
  public OAuth(String provider, String providerId) {
    this(provider, providerId, null);
  }

  public static final String APPLE = "apple";
  public static final String KAKAO = "kakao";
}
