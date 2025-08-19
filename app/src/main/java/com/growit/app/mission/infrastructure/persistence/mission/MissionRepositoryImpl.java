package com.growit.app.mission.infrastructure.persistence.mission;

import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.MissionRepository;
import com.growit.app.mission.infrastructure.persistence.mission.source.DBMissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MissionRepositoryImpl implements MissionRepository {
  private final MissionDBMapper mapper;
  private final DBMissionRepository repository;

  @Override
  public List<Mission> findAllByUserIdAndToday(String userId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
    return repository.findAllByUserIdAndToday(userId, startOfDay, endOfDay).stream()
      .map(mapper::toDomain)
      .toList();
  }
}
