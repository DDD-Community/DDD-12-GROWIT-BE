package com.growit.app.user.controller.mapper;

import com.growit.app.user.controller.dto.request.TokenResponse;
import com.growit.app.user.controller.dto.response.JobRoleResponse;
import com.growit.app.user.controller.dto.response.UserResponse;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.user.dto.UserDto;
import java.util.List;

public class ResponseMapper {

  // Token → TokenResponse
  public static TokenResponse toTokenResponse(Token token) {
    return TokenResponse.builder()
        .accessToken(token.accessToken())
        .refreshToken(token.refreshToken())
        .build();
  }

  // UserDto → UserResponse
  public static UserResponse toUserResponse(UserDto userDto) {
    return UserResponse.builder()
        .id(userDto.user().getId())
        .name(userDto.user().getName())
        .jobRole(userDto.jobRole())
        .email(userDto.user().getEmail().value())
        .careerYear(userDto.user().getCareerYear().name())
        .build();
  }

  // JobRole → JobRoleResponse
  public static JobRoleResponse toJobRoleResponse(JobRole jr) {
    return new JobRoleResponse(jr.getUid(), jr.getName());
  }

  // List<JobRole> → List<JobRoleResponse>
  public static List<JobRoleResponse> toJobRoleResponseList(List<JobRole> jobRoles) {
    return jobRoles.stream().map(ResponseMapper::toJobRoleResponse).toList();
  }

  // 등등 확장...
}
