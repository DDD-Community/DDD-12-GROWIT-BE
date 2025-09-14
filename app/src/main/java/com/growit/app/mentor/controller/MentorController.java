package com.growit.app.mentor.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.mentor.domain.vo.IntimacyLevel;
import com.growit.app.mentor.usecase.GetMentorIntimacyUseCase;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mentor")
@RequiredArgsConstructor
public class MentorController {
  private final GetMentorIntimacyUseCase getMentorIntimacyUseCase;

  @GetMapping("/intimacy")
  public ResponseEntity<ApiResponse<String>> getMentorIntimacy(@AuthenticationPrincipal User user) {
    System.out.println(user);
    IntimacyLevel level = getMentorIntimacyUseCase.execute(user.getId());
    return ResponseEntity.ok(ApiResponse.success(level.getLabel()));
  }
}
