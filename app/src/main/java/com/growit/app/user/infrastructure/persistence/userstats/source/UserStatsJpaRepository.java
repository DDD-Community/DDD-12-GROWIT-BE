package com.growit.app.user.infrastructure.persistence.userstats.source;

import com.growit.app.user.infrastructure.persistence.userstats.source.entity.UserStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatsJpaRepository extends JpaRepository<UserStatsEntity, String> {
    Optional<UserStatsEntity> findByUserId(String userId);
}
