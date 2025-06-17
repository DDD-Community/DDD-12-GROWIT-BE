package com.growit.app.user.domain.user;

import com.growit.app.user.domain.user.vo.Email;

import java.util.Optional;

public interface UserRepository {
  Optional<User> findByEmail(Email email);

  Optional<User> findUserById(String id);

  void saveUser(User user);
}
