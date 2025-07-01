package com.growit.user.domain.jobrole.service;

import com.growit.common.exception.BadRequestException;

public interface JobRoleService {
  void checkJobRoleExist(String id) throws BadRequestException;
}
