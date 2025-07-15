package com.growit.app.resource.domain.jobrole.service;

import com.growit.app.common.exception.BadRequestException;

public interface JobRoleValidator {
  void checkJobRoleExist(String id) throws BadRequestException;
}
