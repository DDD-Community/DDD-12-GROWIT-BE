package com.growit.app.resource.domain.jobrole.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.resource.domain.jobrole.JobRole;

public interface JobRoleQuery {
  void checkJobRoleExist(String id) throws BadRequestException;

  JobRole getJobRoleById(String id) throws NotFoundException;
}
