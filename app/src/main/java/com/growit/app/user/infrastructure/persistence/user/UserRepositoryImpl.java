package com.growit.app.user.infrastructure.persistence.user;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.infrastructure.persistence.user.source.DbUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final UserDbMapper mapper;
  private final DbUserRepository dbUserRepository;

  @Override
  public Optional<User> findByEmail(Email email) {
    return dbUserRepository.findByEmail(email.value()).map(mapper::toDomain);
  }

  @Override
  public Optional<User> findUserById(String id) {
    return dbUserRepository.findByUid(id).map(mapper::toDomain);
  }

  @Override
  public void saveUser(User user) {
    dbUserRepository.save(mapper.toEntity(user));
  }
}
