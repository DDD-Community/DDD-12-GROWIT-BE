package com.growit.app.user.infrastructure.persistence.user.source;

import com.growit.app.user.infrastructure.persistence.user.source.entity.UserJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaEntityRepository extends JpaRepository<UserJpaEntity, Long> {
  Optional<UserJpaEntity> findByEmail(String email);

  Optional<UserJpaEntity> findByUid(String uid);
}
