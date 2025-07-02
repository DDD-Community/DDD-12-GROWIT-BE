package com.growit.app.retrospect.domain.retrospect.service;

import com.growit.app.common.exception.BadRequestException;

public interface RetrospectValidator {
  void checkContent(String content) throws BadRequestException;

  void checkUniqueRetrospect(String planId) throws BadRequestException;
}
