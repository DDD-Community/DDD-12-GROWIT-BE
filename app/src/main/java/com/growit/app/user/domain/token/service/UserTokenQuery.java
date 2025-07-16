package com.growit.app.user.domain.token.service;

import com.growit.app.user.domain.token.UserToken;
import com.growit.app.user.domain.token.service.exception.InvalidTokenException;

public interface UserTokenQuery {
  UserToken getUserToken(String token) throws InvalidTokenException;

  UserToken getUserTokenByUserId(String userId) throws InvalidTokenException;
}
