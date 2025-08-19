package com.growit.app.mission.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface MissionRepository {
  List<Mission> findAllByUserIdAndToday(
      String userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

  void saveMission(Mission mission);
}
