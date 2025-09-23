package com.growit.app.ai.infrastructure.persistence.aiadvice.source;

import com.growit.app.ai.infrastructure.persistence.aiadvice.source.entity.AIAdviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DBAIAdviceRepository extends JpaRepository<AIAdviceEntity, Long> {
    Optional<AIAdviceEntity> findByUid(String uid);
    List<AIAdviceEntity> findByUserIdOrderByCreatedAtDesc(String userId);
    List<AIAdviceEntity> findByUserIdAndGoalIdOrderByCreatedAtDesc(String userId, String goalId);
    List<AIAdviceEntity> findTop10ByUserIdOrderByCreatedAtDesc(String userId);
    boolean existsByUserIdAndCreatedAtBetween(String userId, LocalDateTime start, LocalDateTime end);
    Optional<AIAdviceEntity> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime start, LocalDateTime end);
    boolean existsByUserIdAndGoalIdAndCreatedAtBetween(String userId, String goalId, LocalDateTime start, LocalDateTime end);
    Optional<AIAdviceEntity> findByUserIdAndGoalIdAndCreatedAtBetween(String userId, String goalId, LocalDateTime start, LocalDateTime end);
}
