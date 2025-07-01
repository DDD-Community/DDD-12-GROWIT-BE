package com.growit.app.retrospect.domain.retrospect.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;

public interface RetrospectValidator {
  void validateContent(String content) throws BadRequestException;
  
  void validateUniqueRetrospect(String goalId, String planId) throws BadRequestException;
  
  void validatePlanExists(String goalId, String planId) throws NotFoundException;
}