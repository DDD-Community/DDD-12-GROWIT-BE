package com.growit.app.user.domain.token.service;

import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.user.User;

public interface TokenService {
  Token createToken(User user);

  Token reIssue(String token);

  boolean isValid(String token);

  String getId(String token);
}
