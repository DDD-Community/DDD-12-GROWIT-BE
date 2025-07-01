package com.growit.retrospect.domain.service;

import com.growit.common.exception.BadRequestException;
import com.growit.retrospect.domain.dto.CreateRetrospectCommand;

public interface RetrospectValidator {
  void validateCreateRetrospect(CreateRetrospectCommand command) throws BadRequestException;
}
