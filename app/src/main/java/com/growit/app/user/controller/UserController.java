package com.growit.app.user.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.user.controller.dto.response.UserResponse;
import com.growit.app.user.controller.mapper.ResponseMapper;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.UserDto;
import com.growit.app.user.usecase.GetUserUseCase;
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
  private final ResponseMapper responseMapper;

  @GetMapping("/myprofile")
  public ResponseEntity<ApiResponse<UserResponse>> getUser(@AuthenticationPrincipal User user) {
    UserDto result = getUserUseCase.execute(user);
    UserResponse userResponse = responseMapper.toUserResponse(result);
    return ResponseEntity.ok(ApiResponse.success(userResponse));
  }
}
