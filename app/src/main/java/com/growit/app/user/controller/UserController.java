package com.growit.app.user.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.util.message.MessageService;
import com.growit.app.user.controller.dto.request.UpdateUserRequest;
import com.growit.app.user.controller.dto.response.UserResponse;
import com.growit.app.user.controller.mapper.RequestMapper;
import com.growit.app.user.controller.mapper.ResponseMapper;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.UserDto;
import com.growit.app.user.usecase.DeleteUserUseCase;
import com.growit.app.user.usecase.GetUserUseCase;
import com.growit.app.user.usecase.LogoutUseCase;
import com.growit.app.user.usecase.UpdateUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final GetUserUseCase getUserUseCase;
  private final UpdateUserUseCase updateUserUseCase;
  private final LogoutUseCase logoutUseCase;
  private final DeleteUserUseCase deleteUseCase;
  private final RequestMapper requestMapper;
  private final ResponseMapper responseMapper;
  private final MessageService messageService;

  @GetMapping("/myprofile")
  public ResponseEntity<ApiResponse<UserResponse>> getUser(@AuthenticationPrincipal User user) {
    UserDto result = getUserUseCase.execute(user);
    UserResponse userResponse = responseMapper.toUserResponse(result);
    return ResponseEntity.ok(ApiResponse.success(userResponse));
  }

  @PutMapping("/myprofile")
  public ResponseEntity<ApiResponse<String>> updateUser(
      @AuthenticationPrincipal User user, @Valid @RequestBody UpdateUserRequest body) {
    updateUserUseCase.execute(requestMapper.toUpdateUserCommand(user, body));
    return ResponseEntity.ok(ApiResponse.success(messageService.msg("success.user.update")));
  }

  @PostMapping("/myprofile/logout")
  public ResponseEntity<ApiResponse<String>> logout(@AuthenticationPrincipal User user) {
    logoutUseCase.execute(user);
    return ResponseEntity.ok(ApiResponse.success(messageService.msg("success.user.logout")));
  }

  @DeleteMapping("/myprofile")
  public ResponseEntity<ApiResponse<String>> deleteUser(@AuthenticationPrincipal User user) {
    deleteUseCase.execute(user);
    return ResponseEntity.ok(ApiResponse.success(messageService.msg("success.user.delete")));
  }

  @GetMapping("/myprofile/onboarding")
  public ResponseEntity<ApiResponse<Boolean>> getOnboarding(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(ApiResponse.success(user.isOnboarding()));
  }

  @PutMapping("/myprofile/onboarding")
  public ResponseEntity<ApiResponse<String>> onboarding(@AuthenticationPrincipal User user) {
    updateUserUseCase.isOnboarding(user);
    return ResponseEntity.ok(ApiResponse.success(messageService.msg("success.user.onboarding")));
  }
}
