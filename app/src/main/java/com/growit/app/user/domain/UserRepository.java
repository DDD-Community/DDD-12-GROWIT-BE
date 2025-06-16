package com.growit.app.user.domain;

import com.growit.app.user.domain.vo.Email;

import java.util.Optional;

public interface UserRepository {
  Optional<User> findByEmail(Email email);

  void saveUser(User user);
}
