package com.growit.user.infrastructure.persistence.user.source;

import com.growit.user.infrastructure.persistence.user.source.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBUserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByUid(String uid);
}
