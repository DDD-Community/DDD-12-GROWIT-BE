package com.growit.app.retrospect.domain.retrospect;

import java.util.Optional;

public interface RetrospectRepository {
  void saveRetrospect(Retrospect retrospect);

  Optional<Retrospect> findById(String id);

  Optional<Retrospect> findByIdAndUserId(String id, String userId);

  Optional<Retrospect> findByPlanId(String planId);
}
