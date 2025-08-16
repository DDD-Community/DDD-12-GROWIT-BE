package com.growit.app.goal.controller;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.IdDto;
import com.growit.app.common.util.message.MessageService;
import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.mapper.GoalRequestMapper;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.DeleteGoalCommand;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.CreateGoalUseCase;
import com.growit.app.goal.usecase.DeleteGoalUseCase;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.goal.usecase.UpdateGoalUseCase;
import com.growit.app.user.domain.user.User;
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
  private final MessageService messageService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<Goal>>> getMyGoal(
      @AuthenticationPrincipal User user, @RequestParam() GoalStatus status) {
    status = status == null ? GoalStatus.NONE : status;
    List<Goal> goals = getUserGoalsUseCase.getMyGoals(user, status);
    return ResponseEntity.ok(ApiResponse.success(goals));
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
}
