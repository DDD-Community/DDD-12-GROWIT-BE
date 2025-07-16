package com.growit.app.user.domain.token;

import java.util.Optional;

public interface UserTokenRepository {
  Optional<UserToken> findByToken(String token);

  Optional<UserToken> findByUserId(String userId);

  void saveUserToken(UserToken token);

  void deleteUserToken(UserToken token);
}
