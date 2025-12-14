package com.growit.app.goal.domain.anlaysis;

import java.util.Optional;

public interface AnalysisRepository {
  Optional<GoalAnalysis> findByGoalId(String goalId);
  void save(String goalId, GoalAnalysis analysis);
  void deleteByGoalId(String goalId);
}
