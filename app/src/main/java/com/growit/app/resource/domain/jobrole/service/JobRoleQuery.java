package com.growit.app.resource.domain.jobrole.service;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.resource.domain.jobrole.JobRole;

public interface JobRoleQuery {
  JobRole getJobRoleById(String id) throws NotFoundException;
}
