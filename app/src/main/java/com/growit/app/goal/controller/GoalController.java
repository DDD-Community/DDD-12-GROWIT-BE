package com.growit.app.goal.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.IdDto;
import com.growit.app.common.util.message.MessageService;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.dto.request.UpdatePlanRequest;
import com.growit.app.goal.controller.mapper.GoalRequestMapper;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.DeleteGoalCommand;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.dto.UpdatePlanCommand;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.*;
import com.growit.app.user.domain.user.User;
import com.growit.app.ai.domain.service.AIDataService;
import com.growit.app.ai.domain.aiadvice.AIAdvice;
import com.growit.app.ai.domain.aiadvice.AIAdviceRepository;
import jakarta.validation.Valid;
import java.time.LocalDate;
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
  private final UpdateGoalUseCase updateGoalUseCase;
  private final UpdatePlanUseCase updatePlanUseCase;
  private final MessageService messageService;
  private final GetGoalUseCase getGoalUseCase;
  private final AIDataService aiDataService;
  private final AIAdviceRepository aiAdviceRepository;

  @GetMapping
  public ResponseEntity<ApiResponse<List<Goal>>> getMyGoal(
      @AuthenticationPrincipal User user, @RequestParam(required = false) String status) {
    GoalStatus goalStatus;
    goalStatus = status == null ? GoalStatus.NONE : GoalStatus.valueOf(status);
    List<Goal> goals = getUserGoalsUseCase.getMyGoals(user, goalStatus);
    return ResponseEntity.ok(ApiResponse.success(goals));
  }

  @GetMapping("{id}")
  public ResponseEntity<ApiResponse<Goal>> getGoalById(
      @PathVariable String id, @AuthenticationPrincipal User user) {
    return ResponseEntity.ok(ApiResponse.success(getGoalUseCase.getGoal(id, user)));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<IdDto>> createGoal(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateGoalRequest request) {
    CreateGoalCommand command = goalRequestMapper.toCommand(user.getId(), request);
    String goalId = createGoalUseCase.execute(command);

    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(new IdDto(goalId)));
  }

  @PutMapping("{id}")
  public ResponseEntity<ApiResponse<String>> updateGoal(
      @PathVariable String id,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody CreateGoalRequest request) {
    UpdateGoalCommand command = goalRequestMapper.toUpdateCommand(id, user.getId(), request);
    updateGoalUseCase.execute(command);

    return ResponseEntity.ok(ApiResponse.success(messageService.msg("success.goal.update")));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<ApiResponse<String>> deleteGoal(
      @PathVariable String id, @AuthenticationPrincipal User user) {
    DeleteGoalCommand command = goalRequestMapper.toDeleteCommand(id, user.getId());
    deleteGoalUseCase.execute(command);

    return ResponseEntity.ok(ApiResponse.success(messageService.msg("success.goal.delete")));
  }

  @GetMapping("/utils/ended")
  public ResponseEntity<ApiResponse<String>> getEndedGoals() {
    LocalDate today = LocalDate.now();
    updateGoalUseCase.updateEndedGoals(today);
    return ResponseEntity.ok(ApiResponse.success(messageService.msg("success.goal.status.update")));
  }

  @GetMapping("/me/exists")
  public ResponseEntity<ApiResponse<Boolean>> getGoalIsExist(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(
        ApiResponse.success(!getUserGoalsUseCase.getMyGoals(user, GoalStatus.NONE).isEmpty()));
  }

  @PutMapping("/me/updatePlan")
  public ResponseEntity<ApiResponse<String>> updatePlanContent(
      @RequestParam() String goalId,
      @RequestParam() String planId,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody UpdatePlanRequest request) {
    UpdatePlanCommand command =
        goalRequestMapper.toUpdatePlanCommand(goalId, planId, user.getId(), request);

    updatePlanUseCase.execute(command);
    return ResponseEntity.ok(
        ApiResponse.success(messageService.msg("success.plan.content.update")));
  }

  @GetMapping("/{goalId}/plans/{planId}/recommendation")
  public ResponseEntity<ApiResponse<AIDataService.AIDataResponse>> getPlanRecommendationData(
      @PathVariable String goalId,
      @PathVariable String planId,
      @AuthenticationPrincipal User user) {
    
    AIDataService.AIDataResponse data = aiDataService.getDataForPlanRecommendation(user.getId(), goalId, planId);
    
    return ResponseEntity.ok(ApiResponse.success(data));
  }

  @GetMapping("/{goalId}/ai-advices")
  public ResponseEntity<ApiResponse<List<AIAdvice>>> getAIAdvicesByGoal(
      @PathVariable String goalId,
      @AuthenticationPrincipal User user) {
    
    List<AIAdvice> aiAdvices = aiAdviceRepository.findByUserIdAndGoalId(user.getId(), goalId);
    
    return ResponseEntity.ok(ApiResponse.success(aiAdvices));
  }

  @GetMapping("/ai-advices/recent")
  public ResponseEntity<ApiResponse<List<AIAdvice>>> getRecentAIAdvices(
      @AuthenticationPrincipal User user,
      @RequestParam(defaultValue = "10") int limit) {
    
    List<AIAdvice> aiAdvices = aiAdviceRepository.findRecentByUserId(user.getId(), limit);
    
    return ResponseEntity.ok(ApiResponse.success(aiAdvices));
  }
}
