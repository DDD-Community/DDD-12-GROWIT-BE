package com.growit.app.user.infrastructure.persistence.user.source;

import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DBUserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByEmail(String email);
  Optional<UserEntity> findByUid(String uid);
}
