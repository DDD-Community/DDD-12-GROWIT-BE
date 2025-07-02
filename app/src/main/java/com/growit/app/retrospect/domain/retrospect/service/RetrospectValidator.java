package com.growit.app.retrospect.domain.retrospect.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.retrospect.domain.retrospect.Retrospect;

public interface RetrospectValidator {
  void checkUniqueRetrospect(String planId) throws BadRequestException;

  void checkMyRetrospect(Retrospect retrospect, String userId) throws BadRequestException;
}
