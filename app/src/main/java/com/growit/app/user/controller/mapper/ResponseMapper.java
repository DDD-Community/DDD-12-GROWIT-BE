package com.growit.app.user.controller.mapper;

import com.growit.app.user.controller.dto.response.JobRoleResponse;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.controller.dto.response.UserResponse;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.user.dto.UserDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

  private static JobRoleResponse toJobRoleResponse(JobRole jr) {
    return new JobRoleResponse(jr.getId(), jr.getName());
  }

  public TokenResponse toTokenResponse(Token token) {
    return TokenResponse.builder()
        .accessToken(token.accessToken())
        .refreshToken(token.refreshToken())
        .build();
  }

  public UserResponse toUserResponse(UserDto userDto) {
    return UserResponse.builder()
        .id(userDto.user().getId())
        .name(userDto.user().getName())
        .jobRole(userDto.jobRole())
        .email(userDto.user().getEmail().value())
        .careerYear(userDto.user().getCareerYear().name())
        .build();
  }

  public List<JobRoleResponse> toJobRoleResponseList(List<JobRole> jobRoles) {
    return jobRoles.stream().map(ResponseMapper::toJobRoleResponse).toList();
  }
}
