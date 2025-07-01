package com.growit.user.domain.user.service;

import com.growit.user.domain.user.vo.Email;

public interface UserService {
  void checkEmailExists(Email email) throws AlreadyExistEmailException;
}
