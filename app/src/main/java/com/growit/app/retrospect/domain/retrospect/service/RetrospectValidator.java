package com.growit.app.retrospect.domain.retrospect.service;

import com.growit.app.common.exception.BadRequestException;

public interface RetrospectValidator {
  void checkUniqueRetrospect(String planId) throws BadRequestException;
}
