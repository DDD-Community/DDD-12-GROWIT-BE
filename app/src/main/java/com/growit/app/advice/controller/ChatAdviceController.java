package com.growit.app.advice.controller;

import com.growit.app.advice.controller.dto.request.SendChatAdviceRequest;
import com.growit.app.advice.usecase.GetChatAdviceUseCase;
import com.growit.app.advice.usecase.SendChatAdviceUseCase;
import com.growit.app.common.response.ApiResponse;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advice/chat")
@RequiredArgsConstructor
public class ChatAdviceController {

  private final GetChatAdviceUseCase getChatAdviceUseCase;
  private final SendChatAdviceUseCase sendChatAdviceUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<Object>> getChatAdviceStatus(
      @AuthenticationPrincipal User user, @RequestParam() Integer week) {
    var response = getChatAdviceUseCase.execute(user, week);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Object>> sendChatAdvice(
      @AuthenticationPrincipal User user, @RequestBody SendChatAdviceRequest request) {
    var response =
        sendChatAdviceUseCase.execute(
            user,
            request.getWeek(),
            request.getGoalId(),
            request.getUserMessage(),
            request.getAdviceStyle(),
            request.getIsOnboarding());
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
