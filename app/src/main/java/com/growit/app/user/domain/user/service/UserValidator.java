package com.growit.app.user.domain.user.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.OAuth;

public interface UserValidator {
  void checkEmailExists(Email email) throws AlreadyExistEmailException;

  void checkOAuthExists(OAuth oAuth) throws BadRequestException;
}
