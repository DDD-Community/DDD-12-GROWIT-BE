package com.growit.app.mission.infrastructure.persistence.mission;

import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.MissionRepository;
import com.growit.app.mission.infrastructure.persistence.mission.source.DBMissionRepository;
import com.growit.app.mission.infrastructure.persistence.mission.source.entity.MissionEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MissionRepositoryImpl implements MissionRepository {
  private final MissionDBMapper mapper;
  private final DBMissionRepository repository;

  @Override
  public List<Mission> findAllByUserIdAndToday(
      String userId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
    return repository.findAllByUserIdAndToday(userId, startOfDay, endOfDay).stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public void saveMission(Mission mission) {
    Optional<MissionEntity> existing = repository.findByUid(mission.getId());
    if (existing.isPresent()) {
      MissionEntity exist = existing.get();
      exist.updateByDomain(mission);
      repository.save(exist);
    } else {
      MissionEntity entity = mapper.toEntity(mission);
      repository.save(entity);
    }
  }
}
