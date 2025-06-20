package com.growit.app.user.domain.jobrole.service;

import com.growit.app.common.exception.BadRequestException;

public interface JobRoleService {
  void checkJobRoleExist(String id) throws BadRequestException;
}
