package com.growit.app.user.controller.dto.mapper;

import com.growit.app.user.controller.dto.request.SignUpRequest;
import com.growit.app.user.domain.dto.SignUpCommand;
import com.growit.app.user.domain.vo.Email;
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
}
