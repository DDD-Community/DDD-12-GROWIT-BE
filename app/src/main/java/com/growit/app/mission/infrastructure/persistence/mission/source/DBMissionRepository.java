package com.growit.app.mission.infrastructure.persistence.mission.source;

import com.growit.app.mission.infrastructure.persistence.mission.source.entity.MissionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBMissionRepository
    extends JpaRepository<MissionEntity, Long>, DBMissionQueryRepository {
  Optional<MissionEntity> findByUid(String uid);

  Optional<MissionEntity> findByUidAndUserId(String uid, String userId);
}
