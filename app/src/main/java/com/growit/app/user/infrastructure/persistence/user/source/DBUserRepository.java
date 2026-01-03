package com.growit.app.user.infrastructure.persistence.user.source;

import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBUserRepository extends JpaRepository<UserEntity, Long>, DBUserQueryRepository {
  List<UserEntity> findAllByUidIn(List<String> uids);
}
