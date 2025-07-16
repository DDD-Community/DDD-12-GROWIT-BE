package com.growit.app.user.infrastructure.persistence.user.source;

import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import java.util.Optional;

public interface DBUserQueryRepository {
  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByUid(String uid);
}
