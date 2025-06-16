package com.growit.app.user.infrastructure.persistence.source;

import com.growit.app.user.infrastructure.persistence.source.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaEntityRepository extends JpaRepository<UserJpaEntity, Long> {
  Optional<UserJpaEntity> findByEmail(String email);
}
