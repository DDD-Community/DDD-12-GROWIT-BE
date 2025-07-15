package com.growit.app.resource.domain.jobrole.service;

import static com.growit.app.common.util.message.ErrorCode.RESOURCE_JOBROLE_NOT_FOUND;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.resource.domain.jobrole.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRoleService implements JobRoleQuery {

  private final JobRoleRepository jobRoleRepository;

  @Override
  public void checkJobRoleExist(String id) {
    if (jobRoleRepository.findById(id).isEmpty()) {
      throw new BadRequestException(RESOURCE_JOBROLE_NOT_FOUND.getCode());
    }
  }

  @Override
  public JobRole getJobRoleById(String id) throws NotFoundException {
    return jobRoleRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(RESOURCE_JOBROLE_NOT_FOUND.getCode()));
  }
}
