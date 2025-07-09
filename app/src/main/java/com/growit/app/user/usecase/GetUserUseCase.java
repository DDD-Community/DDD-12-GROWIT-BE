package com.growit.app.user.usecase;

import static com.growit.app.common.util.message.ErrorCode.RESOURCE_JOBROLE_NOT_FOUND;

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
            .orElseThrow(() -> new BadRequestException(RESOURCE_JOBROLE_NOT_FOUND.getCode()));

    return new UserDto(user, jobRole);
  }
}
