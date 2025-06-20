package com.growit.app.user.usecase;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.jobrole.repository.JobRoleRepository;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserUseCase {
  private final JobRoleRepository jobRoleRepository;

  public UserDto execute(User user) {
    final JobRole jobRole =
        jobRoleRepository
            .findById(user.getJobRoleId())
            .orElseThrow(() -> new BadRequestException("직무가 존재하지 않습니다"));

    return new UserDto(user, jobRole);
  }
}
