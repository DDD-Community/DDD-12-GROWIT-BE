package com.growit.app.user.infrastructure.persistence.userstats;

import com.growit.app.user.domain.userstats.UserStats;
import com.growit.app.user.domain.userstats.UserStatsRepository;
import com.growit.app.user.infrastructure.persistence.userstats.source.UserStatsJpaRepository;
import com.growit.app.user.infrastructure.persistence.userstats.source.entity.UserStatsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStatsRepositoryImpl implements UserStatsRepository {

  private final UserStatsJpaRepository userStatsJpaRepository;

  @Override
  public UserStats findByUserId(String userId) {
    return userStatsJpaRepository
        .findByUserId(userId)
        .map(UserStatsEntity::toDomain)
        .orElse(createDefaultUserStats(userId));
  }

  @Override
  public void save(UserStats userStats) {
    userStatsJpaRepository
        .findByUserId(userStats.getUserId())
        .ifPresentOrElse(
            entity -> {
              entity.updateByDomain(userStats);
              userStatsJpaRepository.save(entity);
            },
            () -> userStatsJpaRepository.save(UserStatsEntity.from(userStats)));
  }

  private UserStats createDefaultUserStats(String userId) {
    return new UserStats(userId, null, 0);
  }
}
