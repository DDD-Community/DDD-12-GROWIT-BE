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
import org.springframework.transaction.annotation.Transactional;

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

  @Override
  public Optional<Mission> findByIdAndUserId(String id, String userId) {
    Optional<MissionEntity> missionEntity = repository.findByUidAndUserId(id, userId);
    return missionEntity.map(mapper::toDomain);
  }

  @Override
  @Transactional
  public void saveAll(List<Mission> missions) {
    List<MissionEntity> entities = missions.stream().map(mapper::toEntity).toList();
    repository.saveAll(entities);
  }

  @Override
  public List<String> findUserIdsHavingContentToday(
      List<String> userIds, String content, LocalDateTime startOfDay, LocalDateTime endOfDay) {

    if (userIds == null || userIds.isEmpty()) return List.of();
    return repository.findUserIdsHavingContentBetween(userIds, content, startOfDay, endOfDay);
  }
}
