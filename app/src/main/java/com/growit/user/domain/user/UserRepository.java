package com.growit.user.domain.user;

import com.growit.user.domain.user.vo.Email;
import java.util.Optional;

public interface UserRepository {
  Optional<User> findByEmail(Email email);

  Optional<User> findUserByuId(String id);

  void saveUser(User user);
}
