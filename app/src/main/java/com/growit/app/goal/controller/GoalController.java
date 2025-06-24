package com.growit.app.goal.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.dto.response.IdDto;
import com.growit.app.goal.controller.mapper.GoalRequestMapper;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.usecase.CreateGoalUseCase;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {
  private final CreateGoalUseCase createGoalUseCase;
  private final GoalRequestMapper goalRequestMapper;

  @PostMapping
  public ResponseEntity<ApiResponse<IdDto>> createGoal(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateGoalRequest request) {
    CreateGoalCommand command = goalRequestMapper.toCreateGoalCommand(user.getId(), request);
    String goalId = createGoalUseCase.execute(command);
    IdDto response = IdDto.builder().id(goalId).build();
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
  }
}