package com.growit.app.user.infrastructure.persistence;

import com.growit.app.user.domain.User;
import com.growit.app.user.domain.UserRepository;
import com.growit.app.user.domain.vo.Email;
import com.growit.app.user.infrastructure.persistence.source.UserJpaEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final UserMapper mapper;
  private final UserJpaEntityRepository userJpaEntityRepository;

  @Override
  public Optional<User> findByEmail(Email email) {
    return userJpaEntityRepository.findByEmail(email.value())
      .map(mapper::toDomain);
  }

  @Override
  public void saveUser(User user) {
    userJpaEntityRepository.save(mapper.toEntity(user));
  }
}
