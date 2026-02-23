package com.growit.app.user.controller.mapper;

import com.growit.app.user.controller.dto.response.SajuInfoResponse;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.controller.dto.response.UserResponse;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

  public TokenResponse toTokenResponse(Token token) {
    return TokenResponse.builder()
        .accessToken(token.accessToken())
        .refreshToken(token.refreshToken())
        .build();
  }

  public UserResponse toUserResponse(UserDto userDto) {
    SajuInfoResponse sajuInfoResponse = null;
    if (userDto.user().getSaju() != null) {
      sajuInfoResponse = SajuInfoResponse.from(userDto.user().getSaju());
    }

    return UserResponse.builder()
        .id(userDto.user().getId())
        .name(userDto.user().getName())
        .lastName(userDto.user().getLastName())
        .email(userDto.user().getEmail().value())
        .saju(sajuInfoResponse)
        .careerYear(userDto.user().getCareerYear())
        .jobRoleId(userDto.user().getJobRoleId())
        .build();
  }
}
