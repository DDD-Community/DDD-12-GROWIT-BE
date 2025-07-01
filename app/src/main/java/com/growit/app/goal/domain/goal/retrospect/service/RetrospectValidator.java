package com.growit.app.goal.domain.goal.retrospect.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;

public interface RetrospectValidator {
  void validateContent(String content) throws BadRequestException;
  
  void validatePlanExists(String goalId, String planId) throws NotFoundException;
  
  void validateUniqueRetrospect(String goalId, String planId) throws BadRequestException;
}