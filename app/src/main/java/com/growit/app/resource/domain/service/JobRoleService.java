package com.growit.app.resource.domain.service;

import com.growit.app.common.error.BadRequestError;
import com.growit.app.resource.domain.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRoleService implements JobRoleValidator {
  private final JobRoleRepository jobRoleRepository;

  @Override
  public void checkJobRoleExists(String id) {
    if (jobRoleRepository.findById(id).isEmpty()) {
      throw new BadRequestError("직무가 존재하지 않습니다");
    }
  }
}
