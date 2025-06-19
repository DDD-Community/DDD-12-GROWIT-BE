package com.growit.app.user.domain.token.service;

import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.user.User;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
  @Override
  public Token createToken(User user) {
    return null;
  }
}
