package com.growit.app.retrospect.domain.retrospect.service;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.retrospect.domain.retrospect.Retrospect;

public interface RetrospectQuery {
  Retrospect getMyRetrospect(String id, String userId) throws NotFoundException;
}
