package com.growit.app.user.domain.token.service;

import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.token.service.error.ExpiredTokenException;
import com.growit.app.user.domain.token.service.error.InvalidTokenException;
import com.growit.app.user.domain.user.User;

public interface TokenService {
  Token createToken(User user);

  Token reIssue(String token);

  String getId(String token) throws InvalidTokenException, ExpiredTokenException;
}
