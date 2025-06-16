package com.growit.app.user.domain.service;

import com.growit.app.auth.domain.dto.SignUpCommand;

public interface UserService {
  void signup(SignUpCommand command);
}
