package com.growit.app.mission.infrastructure.persistence.mission.source;

import com.growit.app.mission.infrastructure.persistence.mission.source.entity.MissionEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DBMissionQueryRepository {
  List<MissionEntity> findAllByUserIdAndToday(String userId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
