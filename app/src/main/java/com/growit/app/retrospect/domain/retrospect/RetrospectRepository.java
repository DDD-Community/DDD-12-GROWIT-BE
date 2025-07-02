package com.growit.app.retrospect.domain.retrospect;

import java.util.Optional;

public interface RetrospectRepository {
  void saveRetrospect(Retrospect retrospect);

  Optional<Retrospect> findByGoalIdAndPlanId(String goalId, String planId);
}
