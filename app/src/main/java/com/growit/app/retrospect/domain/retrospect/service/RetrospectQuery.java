package com.growit.app.retrospect.domain.retrospect.service;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectQueryFilter;

public interface RetrospectQuery {
  Retrospect getMyRetrospect(String id, String userId) throws NotFoundException;

  Retrospect getRetrospectByFilter(RetrospectQueryFilter filter) throws NotFoundException;
}
