package com.growit.app.user.controller;

import com.growit.app.common.dto.Response;
import com.growit.app.user.controller.dto.response.UserResponse;
import com.growit.app.user.controller.mapper.UserRequestMapper;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.usecase.GetUserUseCase;
import com.growit.app.user.usecase.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final GetUserUseCase getUserUseCase;
  private final UserRequestMapper mapper;

  @GetMapping("/myprofile")
  public ResponseEntity<Response<UserResponse>> getUser(@AuthenticationPrincipal User user) {
    UserDto userDto = getUserUseCase.execute(user);
    return ResponseEntity.ok(Response.ok(mapper.toResponse(userDto)));
  }
}
