package com.growit.app.mission.infrastructure.persistence.mission.source;

import com.growit.app.mission.infrastructure.persistence.mission.source.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBMissionRepository
    extends JpaRepository<MissionEntity, Long>, DBMissionQueryRepository {}
