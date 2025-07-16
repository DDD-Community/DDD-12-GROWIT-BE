package com.growit.app.user.domain.token.service;

import com.growit.app.user.domain.token.service.exception.ExpiredTokenException;
import com.growit.app.user.domain.token.service.exception.InvalidTokenException;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;

public interface TokenGenerator {
  Token createToken(User user);

  String getId(String token) throws InvalidTokenException, ExpiredTokenException;

  Token reIssue(String token);
}
