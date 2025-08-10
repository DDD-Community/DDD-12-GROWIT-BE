package com.growit.app.retrospect.domain.retrospect;

import com.growit.app.retrospect.domain.retrospect.dto.RetrospectQueryFilter;
import java.util.List;
import java.util.Optional;

public interface RetrospectRepository {
  void saveRetrospect(Retrospect retrospect);

  Optional<Retrospect> findById(String id);

  Optional<Retrospect> findByIdAndUserId(String id, String userId);

  Optional<Retrospect> findByPlanId(String planId);

  Optional<Retrospect> findByFilter(RetrospectQueryFilter filter);

  List<Retrospect> findByGoalIdAndUserId(String goalId, String userId);
}
