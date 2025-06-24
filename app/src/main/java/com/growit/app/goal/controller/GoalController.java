package com.growit.app.goal.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.goal.controller.dto.GoalResponse;
import com.growit.app.goal.usecase.GetGoalUseCase;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goal")
@RequiredArgsConstructor
public class GoalController {
  private final GetGoalUseCase getGoalUseCase;

  @GetMapping("")
  public ResponseEntity<ApiResponse<GoalResponse>> getMyGoal(@AuthenticationPrincipal User user) {
    GoalResponse goalResponse = getGoalUseCase.getMyGoal(user);
    return ResponseEntity.ok(ApiResponse.success(goalResponse));
  }
}
