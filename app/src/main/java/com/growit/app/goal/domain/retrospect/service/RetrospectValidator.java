package com.growit.app.goal.domain.retrospect.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.goal.domain.retrospect.dto.CreateRetrospectCommand;

public interface RetrospectValidator {
  void validateCreateRetrospect(CreateRetrospectCommand command) throws BadRequestException;
}
