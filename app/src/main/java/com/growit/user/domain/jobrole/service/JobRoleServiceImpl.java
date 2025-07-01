package com.growit.user.domain.jobrole.service;

import com.growit.common.exception.BadRequestException;
import com.growit.user.domain.jobrole.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRoleServiceImpl implements JobRoleService {

  private final JobRoleRepository jobRoleRepository;

  @Override
  public void checkJobRoleExist(String id) {
    if (jobRoleRepository.findById(id).isEmpty()) {
      throw new BadRequestException("직무가 존재하지 않습니다");
    }
  }
}
