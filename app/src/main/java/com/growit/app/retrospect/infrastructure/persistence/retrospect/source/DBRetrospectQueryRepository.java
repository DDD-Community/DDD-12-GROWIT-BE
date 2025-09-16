package com.growit.app.retrospect.infrastructure.persistence.retrospect.source;

import com.growit.app.retrospect.domain.retrospect.dto.RetrospectQueryFilter;
import com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
import java.util.List;
import java.util.Optional;

public interface DBRetrospectQueryRepository {
  Optional<RetrospectEntity> findByUidAndUserId(String uid, String userId);

  Optional<RetrospectEntity> findByPlanId(String planId);

  Optional<RetrospectEntity> findByFilter(RetrospectQueryFilter filter);

  List<RetrospectEntity> findByGoalIdAndUserId(String goalId, String userId);

  int countWeeklyRetrospectsByUserId(String userId);
}
