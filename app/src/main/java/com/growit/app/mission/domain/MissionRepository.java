package com.growit.app.mission.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MissionRepository {
  List<Mission> findAllByUserIdAndToday(
      String userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

  void saveMission(Mission mission);

  Optional<Mission> findByIdAndUserId(String id, String userId);

  void saveAll(List<Mission> missions);

  List<String> findUserIdsHavingContentToday(
      List<String> userIds, String content, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
