package com.growit.app.user.usecase.dto;

import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDto {
  private final User user;
  private final JobRole jobRole;
}
