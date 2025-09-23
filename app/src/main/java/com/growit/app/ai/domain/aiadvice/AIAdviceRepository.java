package com.growit.app.ai.domain.aiadvice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AIAdviceRepository {
    void save(AIAdvice aiAdvice);
    Optional<AIAdvice> findById(String id);
    List<AIAdvice> findByUserId(String userId);
    List<AIAdvice> findByUserIdAndGoalId(String userId, String goalId);
    List<AIAdvice> findRecentByUserId(String userId, int limit);
    boolean existsByUserIdAndDate(String userId, LocalDate date);
    Optional<AIAdvice> findByUserIdAndDate(String userId, LocalDate date);
    boolean existsByUserIdAndGoalIdAndDate(String userId, String goalId, LocalDate date);
    Optional<AIAdvice> findByUserIdAndGoalIdAndDate(String userId, String goalId, LocalDate date);
}
