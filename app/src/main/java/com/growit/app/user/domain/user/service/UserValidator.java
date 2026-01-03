package com.growit.app.user.domain.user.service;

import com.growit.app.user.domain.user.vo.Email;

public interface UserValidator {
  void checkEmailExists(Email email) throws AlreadyExistEmailException;
}
