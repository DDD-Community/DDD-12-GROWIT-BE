package com.growit.user.domain.token.service;

import com.growit.user.domain.token.Token;
import com.growit.user.domain.token.service.exception.ExpiredTokenException;
import com.growit.user.domain.token.service.exception.InvalidTokenException;
import com.growit.user.domain.user.User;

public interface TokenService {
  Token createToken(User user);

  String getId(String token) throws InvalidTokenException, ExpiredTokenException;

  Token reIssue(String token);
}
