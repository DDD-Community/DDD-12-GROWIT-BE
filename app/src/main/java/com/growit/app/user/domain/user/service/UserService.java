package com.growit.app.user.domain.user.service;

import com.growit.app.user.domain.user.vo.Email;

public interface UserService {
  void checkEmailExists(Email email) throws AlreadyExistEmailException;
}
