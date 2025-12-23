package com.growit.app.advice.controller;

import com.growit.app.advice.usecase.GetChatAdviceUseCase;
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

  @GetMapping
  public ResponseEntity<ApiResponse<Object>> getChatAdviceStatus(
      @AuthenticationPrincipal User user, @RequestParam() Integer week) {
    var response = getChatAdviceUseCase.execute(user, week);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  /** 조언 받을 목표와 멘토를 선택하여 조언을 네스트로 요청하고 그의 결과 값을 return 한다. */
  @PostMapping
  public ResponseEntity<ApiResponse<Object>> sendChatAdvice(
      @AuthenticationPrincipal User user, @RequestBody Object request) {
    // TODO :: UseCase 구현 후 주석 해제
    // SendChatAdviceCommand command = requestMapper.toCommand(user.getId(), request);
    // ChatAdviceResult result = sendChatAdviceUseCase.execute(command);
    // return ResponseEntity.ok(ApiResponse.success(result));
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
