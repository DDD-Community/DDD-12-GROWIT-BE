package com.growit.app.user.domain.user;

import com.growit.app.user.domain.user.vo.Email;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {
  Optional<User> findByEmail(Email email);

  Optional<User> findUserByuId(String id);

  void saveUser(User user);

  Page<User> findAll(Pageable pageable);
}
