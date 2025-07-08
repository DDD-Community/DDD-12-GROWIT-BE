package com.growit.app.retrospect.infrastructure.persistence.retrospect.source;

import com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity.RetrospectEntity;
import java.util.Optional;

public interface DBRetrospectQueryRepository {
  Optional<RetrospectEntity> findByUidAndUserId(String uid, String userId);

  Optional<RetrospectEntity> findByPlanId(String planId);
}
