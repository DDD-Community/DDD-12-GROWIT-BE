package com.growit.app.user.infrastructure.persistence.userstats.source;

import com.growit.app.user.infrastructure.persistence.userstats.source.entity.UserStatsEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatsJpaRepository extends JpaRepository<UserStatsEntity, String> {
  Optional<UserStatsEntity> findByUserId(String userId);

  List<UserStatsEntity> findByUserIdIn(List<String> userIds);
}
