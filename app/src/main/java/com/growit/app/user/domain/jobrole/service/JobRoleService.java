package com.growit.app.user.domain.jobrole.service;

import com.growit.app.common.error.BadRequestException;
import com.growit.app.user.domain.jobrole.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRoleService implements JobRoleValidator {
  private final JobRoleRepository jobRoleRepository;

  @Override
  public void checkJobRoleExists(String id) {
    if (jobRoleRepository.findById(id).isEmpty()) {
      throw new BadRequestException("직무가 존재하지 않습니다");
    }
  }
}
