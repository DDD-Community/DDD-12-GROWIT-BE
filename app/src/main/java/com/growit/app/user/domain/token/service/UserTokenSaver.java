package com.growit.app.user.domain.token.service;

import com.growit.app.user.domain.token.vo.Token;

public interface UserTokenSaver {
  void saveUserToken(String userId, Token token);
}
