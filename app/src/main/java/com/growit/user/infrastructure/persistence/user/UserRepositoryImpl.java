package com.growit.user.infrastructure.persistence.user;

import com.growit.user.domain.user.User;
import com.growit.user.domain.user.UserRepository;
import com.growit.user.domain.user.vo.Email;
import com.growit.user.infrastructure.persistence.user.source.DBUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final UserDBMapper userDBMapper;
  private final DBUserRepository dbUserRepository;

  @Override
  public Optional<User> findByEmail(Email email) {
    return dbUserRepository.findByEmail(email.value()).map(userDBMapper::toDomain);
  }

  @Override
  public Optional<User> findUserByuId(String id) {
    return dbUserRepository.findByUid(id).map(userDBMapper::toDomain);
  }

  @Override
  public void saveUser(User user) {
    dbUserRepository.save(userDBMapper.toEntity(user));
  }
}
