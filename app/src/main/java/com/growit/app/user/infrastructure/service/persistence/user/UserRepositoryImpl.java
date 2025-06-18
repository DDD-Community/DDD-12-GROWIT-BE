package com.growit.app.user.infrastructure.service.persistence.user;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.repository.UserRepository;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.infrastructure.service.persistence.user.source.DBUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final DBUserRepository dbUserRepository;

  @Override
  public Optional<User> findByEmail(Email email) {
    return Optional.empty();
  }

  @Override
  public void save(User user) {

  }
}
