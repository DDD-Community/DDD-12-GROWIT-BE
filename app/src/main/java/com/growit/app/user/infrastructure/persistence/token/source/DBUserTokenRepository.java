package com.growit.app.user.infrastructure.persistence.token.source;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface DbUserTokenQueryRepository {
  Optional<UserTokenEntity> findByUserId(String userId);

  Optional<UserTokenEntity> findByToken(String token);

  Optional<UserTokenEntity> findByUid(String uid);
}

public interface DBUserTokenRepository
    extends JpaRepository<UserTokenEntity, Long>, DbUserTokenQueryRepository {}
