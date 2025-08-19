package com.growit.app.mission.infrastructure.persistence.mission.source;

import com.growit.app.mission.infrastructure.persistence.mission.source.entity.MissionEntity;
import com.growit.app.mission.infrastructure.persistence.mission.source.entity.QMissionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DBMissionQueryRepositoryImpl implements DBMissionQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<MissionEntity> findAllByUserIdAndToday(
      String userId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
    QMissionEntity mission = QMissionEntity.missionEntity;
    return queryFactory
        .selectFrom(mission)
        .where(
            mission.userId.eq(userId),
            mission.deletedAt.isNull(),
            mission.createdAt.between(startOfDay, endOfDay))
        .fetch();
  }
}
