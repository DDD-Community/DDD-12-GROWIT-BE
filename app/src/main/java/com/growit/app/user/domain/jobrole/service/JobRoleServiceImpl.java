package com.growit.app.user.domain.jobrole.service;

import static com.growit.app.common.util.message.ErrorCode.RESOURCE_JOBROLE_NOT_FOUND;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.user.domain.jobrole.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRoleServiceImpl implements JobRoleService {

  private final JobRoleRepository jobRoleRepository;

  @Override
  public void checkJobRoleExist(String id) {
    if (jobRoleRepository.findById(id).isEmpty()) {
      throw new BadRequestException(RESOURCE_JOBROLE_NOT_FOUND.getCode());
    }
  }
}
