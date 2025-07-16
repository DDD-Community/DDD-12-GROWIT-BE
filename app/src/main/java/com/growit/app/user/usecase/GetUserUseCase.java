package com.growit.app.user.usecase;

import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.resource.domain.jobrole.service.JobRoleQuery;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserUseCase {
  private final JobRoleQuery jobRoleQuery;

  public UserDto execute(User user) {
    final JobRole jobRole = jobRoleQuery.getJobRoleById(user.getJobRoleId());

    return new UserDto(user, jobRole);
  }
}
