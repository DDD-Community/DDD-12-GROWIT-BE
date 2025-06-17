package com.growit.app.user.controller.mapper;

import com.growit.app.user.controller.dto.request.SignUpRequest;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.vo.Email;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {
  public SignUpCommand toCommand(SignUpRequest request) {
    return new SignUpCommand(
      new Email(request.getEmail()),
      request.getPassword(),
      request.getName(),
      request.getJobRoleId(),
      request.getCareerYear()
    );
  }

  public TokenResponse toResponse(Token token) {
    return TokenResponse.builder()
      .accessToken(token.access())
      .refreshToken(token.refresh())
      .build();
  }
}
