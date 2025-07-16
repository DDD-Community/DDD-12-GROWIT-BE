package com.growit.app.user.infrastructure.persistence.token;

import com.growit.app.user.domain.token.UserToken;
import com.growit.app.user.domain.token.UserTokenRepository;
import com.growit.app.user.infrastructure.persistence.token.source.DBUserTokenRepository;
import com.growit.app.user.infrastructure.persistence.token.source.UserTokenEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTokenRepositoryImpl implements UserTokenRepository {
  private final DBUserTokenRepository dbUserTokenRepository;
  private final UserTokenDBMapper mapper;

  @Override
  public Optional<UserToken> findByToken(String token) {
    return dbUserTokenRepository.findByToken(token).map(mapper::toDomain);
  }

  @Override
  public Optional<UserToken> findByUserId(String userId) {
    return dbUserTokenRepository.findByUserId(userId).map(mapper::toDomain);
  }

  @Override
  public void saveUserToken(UserToken token) {
    Optional<UserTokenEntity> existingToken = dbUserTokenRepository.findByUid(token.getId());
    if (existingToken.isPresent()) {
      UserTokenEntity entity = existingToken.get();
      entity.updateByDomain(token);
      dbUserTokenRepository.save(entity);
    } else {
      dbUserTokenRepository.save(mapper.toEntity(token));
    }
  }

  @Override
  public void deleteUserToken(UserToken token) {
    dbUserTokenRepository.findByToken(token.getToken()).ifPresent(dbUserTokenRepository::delete);
  }
}
