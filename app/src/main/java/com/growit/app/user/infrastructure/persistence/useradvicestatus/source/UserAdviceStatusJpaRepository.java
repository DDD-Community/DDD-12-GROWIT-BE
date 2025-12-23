package com.growit.app.user.infrastructure.persistence.useradvicestatus.source;

import com.growit.app.user.infrastructure.persistence.useradvicestatus.source.entity.UserAdviceStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAdviceStatusJpaRepository
    extends JpaRepository<UserAdviceStatusEntity, String> {
  Optional<UserAdviceStatusEntity> findByUserId(String userId);
}
