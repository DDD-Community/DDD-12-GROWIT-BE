package com.growit.app.user.domain.token;

public interface UserTokenRepository {
  boolean isExistToken(String userId, String token);

  void save(String userId, String token);

  void deleteByUserId(String userId);

  void update(String userId, String token);
}
