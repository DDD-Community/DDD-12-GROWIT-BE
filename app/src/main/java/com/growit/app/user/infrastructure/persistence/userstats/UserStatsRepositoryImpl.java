package com.growit.app.user.infrastructure.persistence.userstats;

import com.growit.app.user.domain.userstats.UserStats;
import com.growit.app.user.domain.userstats.UserStatsRepository;
import com.growit.app.user.infrastructure.persistence.userstats.source.entity.UserStatsEntity;
import com.growit.app.user.infrastructure.persistence.userstats.source.UserStatsJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class UserStatsRepositoryImpl implements UserStatsRepository {

    private final UserStatsJpaRepository userStatsJpaRepository;

    @Override
    public UserStats findByUserId(String userId) {
        return userStatsJpaRepository.findByUserId(userId)
                .map(UserStatsEntity::toDomain)
                .orElse(createDefaultUserStats(userId));
    }

    private UserStats createDefaultUserStats(String userId) {
        return new UserStats(userId, LocalDate.now(), 0);
    }
}
