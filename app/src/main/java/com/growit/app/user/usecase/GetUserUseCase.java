package com.growit.app.user.usecase;

import com.growit.app.common.error.BadRequestError;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.jobrole.JobRoleRepository;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.usecase.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserUseCase {
  private final JobRoleRepository jobRoleRepository;

  @Transactional
  public UserDto execute(User user) {
    JobRole jobRole =
        jobRoleRepository
            .findById(user.getJobRoleId())
            .orElseThrow(() -> new BadRequestError("직무가 존재하지 않습니다"));

    return new UserDto(user, jobRole);
  }
}
