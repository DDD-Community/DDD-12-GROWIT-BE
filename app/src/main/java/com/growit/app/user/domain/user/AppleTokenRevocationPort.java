package com.growit.app.user.domain.user;

public interface AppleTokenRevocationPort {
  void revoke(String refreshToken);
}
