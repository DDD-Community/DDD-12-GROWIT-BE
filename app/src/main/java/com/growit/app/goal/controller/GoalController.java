package com.growit.app.goal.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.goal.controller.dto.GoalResponse;
import com.growit.app.goal.usecase.GetGoalUseCase;
import com.growit.app.user.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {
  private final GetGoalUseCase getGoalUseCase;

  @GetMapping("")
  public ResponseEntity<ApiResponse<List<GoalResponse>>> getMyGoal(
      @AuthenticationPrincipal User user) {
    List<GoalResponse> goals = getGoalUseCase.getMyGoals(user);
    return ResponseEntity.ok(ApiResponse.success(goals));
  }
}
