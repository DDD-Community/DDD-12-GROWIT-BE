package com.growit.app.goal.domain.goal.retrospect;

import java.util.Optional;

public interface RetrospectRepository {
  void saveRetrospect(Retrospect retrospect);
  
  Optional<Retrospect> findByGoalIdAndPlanId(String goalId, String planId);
  
  Optional<Retrospect> findById(String id);
}