package com.growit.app.user.infrastructure.persistence.user.source;

import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBUserRepository extends JpaRepository<UserEntity, Long>, DBUserQueryRepository {
  java.util.List<UserEntity> findAllByUidIn(java.util.List<String> uids);
}
