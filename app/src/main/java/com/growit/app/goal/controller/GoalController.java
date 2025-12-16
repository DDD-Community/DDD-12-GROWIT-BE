package com.growit.app.goal.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.dto.response.GoalCreateResponse;
import com.growit.app.goal.controller.dto.response.GoalDetailResponse;
import com.growit.app.goal.controller.mapper.GoalRequestMapper;
import com.growit.app.goal.controller.mapper.GoalResponseMapper;
import com.growit.app.goal.domain.goal.dto.*;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.*;
import com.growit.app.goal.usecase.dto.GoalWithAnalysisDto;
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
  private final GoalResponseMapper goalResponseMapper;
  private final GetUserGoalsUseCase getUserGoalsUseCase;
  private final DeleteGoalUseCase deleteGoalUseCase;
  private final UpdateGoalUseCase updateGoalUseCase;
  private final GetGoalUseCase getGoalUseCase;

  @GetMapping
  public ResponseEntity<ApiResponse<List<GoalDetailResponse>>> getMyGoals(
      @AuthenticationPrincipal User user, @RequestParam(required = false) String status) {
    GoalStatus goalStatus = goalResponseMapper.mapToGoalStatus(status);
    List<GoalWithAnalysisDto> goalWithAnalysis = getUserGoalsUseCase.getMyGoals(user, goalStatus);
    List<GoalDetailResponse> responses =
        goalWithAnalysis.stream()
            .map(dto -> goalResponseMapper.toDetailResponse(dto.getGoal(), dto.getAnalysis()))
            .toList();

    return ResponseEntity.ok(ApiResponse.success(responses));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<GoalDetailResponse>> getGoalById(
      @PathVariable String id, @AuthenticationPrincipal User user) {
    GoalWithAnalysisDto goalWithAnalysis = getGoalUseCase.getGoal(id, user);

    return ResponseEntity.ok(
        ApiResponse.success(
            goalResponseMapper.toDetailResponse(
                goalWithAnalysis.getGoal(), goalWithAnalysis.getAnalysis())));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<GoalCreateResponse>> createGoal(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateGoalRequest request) {
    CreateGoalCommand command = goalRequestMapper.toCommand(user.getId(), request);
    CreateGoalResult result = createGoalUseCase.execute(command);
    GoalWithAnalysisDto goalWithAnalysis = getGoalUseCase.getGoal(result.id(), user);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(goalResponseMapper.toCreateResponse(goalWithAnalysis.getGoal())));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> updateGoal(
      @PathVariable String id,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody CreateGoalRequest request) {
    UpdateGoalCommand command = goalRequestMapper.toUpdateCommand(id, user.getId(), request);
    updateGoalUseCase.execute(command);

    return ResponseEntity.ok(ApiResponse.success("목표가 수정 완료되었습니다."));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteGoal(
      @PathVariable String id, @AuthenticationPrincipal User user) {
    DeleteGoalCommand command = goalRequestMapper.toDeleteCommand(id, user.getId());
    deleteGoalUseCase.execute(command);

    return ResponseEntity.ok(ApiResponse.success("삭제되었습니다."));
  }
}
