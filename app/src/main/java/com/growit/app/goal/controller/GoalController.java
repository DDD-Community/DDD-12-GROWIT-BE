package com.growit.app.goal.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.IdDto;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.mapper.GoalRequestMapper;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.DeleteGoalCommand;
import com.growit.app.goal.usecase.CreateGoalUseCase;
import com.growit.app.goal.usecase.DeleteGoalUseCase;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {
  private final CreateGoalUseCase createGoalUseCase;
  private final GoalRequestMapper goalRequestMapper;
  private final GetUserGoalsUseCase getUserGoalsUseCase;
  private final DeleteGoalUseCase deleteGoalUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<List<Goal>>> getMyGoal(@AuthenticationPrincipal User user) {
    List<Goal> goals = getUserGoalsUseCase.getMyGoals(user);
    return ResponseEntity.ok(ApiResponse.success(goals));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<IdDto>> createGoal(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateGoalRequest request) {
    CreateGoalCommand command = goalRequestMapper.toCommand(user.getId(), request);
    String goalId = createGoalUseCase.execute(command);

    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(new IdDto(goalId)));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<ApiResponse<String>> deleteGoal(
      @PathVariable String id, @AuthenticationPrincipal User user) {
    DeleteGoalCommand command = goalRequestMapper.toDeleteCommand(id, user.getId());
    deleteGoalUseCase.execute(command);
    return ResponseEntity.ok(ApiResponse.success("삭제가 완료 되었습니다."));
  }
}
