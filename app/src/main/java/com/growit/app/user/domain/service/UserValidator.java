package com.growit.app.user.domain.service;

import com.growit.app.user.domain.vo.Email;

public interface UserValidator {
  void checkEmailExists(Email email);
}
