package com.growit.app.goal.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.goal.controller.dto.GoalResponse;
import com.growit.app.goal.usecase.GetGoalUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goal")
@RequiredArgsConstructor
public class GoalController {
  private final GetGoalUseCase getGoalUseCase;

  @GetMapping("/{uid}")
  public ResponseEntity<ApiResponse<GoalResponse>> getMyGoal(@PathVariable String uid){
    GoalResponse goalResponse = getGoalUseCase.execute(uid);
    return ResponseEntity.ok(ApiResponse.success(goalResponse));
  }
}
