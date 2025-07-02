package com.growit.app.retrospect.domain.retrospect.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;

public interface RetrospectValidator {
  void checkContent(String content) throws BadRequestException;

  void checkUniqueRetrospect(String goalId, String planId) throws BadRequestException;

  void checkPlanExists(String goalId, String planId) throws NotFoundException;
}
