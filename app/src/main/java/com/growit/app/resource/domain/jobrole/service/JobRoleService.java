package com.growit.app.resource.domain.jobrole.service;

import static com.growit.app.common.util.message.ErrorCode.RESOURCE_JOBROLE_NOT_FOUND;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.resource.domain.jobrole.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRoleService implements JobRoleValidator {

  private final JobRoleRepository jobRoleRepository;

  @Override
  public void checkJobRoleExist(String id) {
    if (jobRoleRepository.findById(id).isEmpty()) {
      throw new BadRequestException(RESOURCE_JOBROLE_NOT_FOUND.getCode());
    }
  }
}
