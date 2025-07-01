package com.growit.goal.controller;

import com.growit.common.response.ApiResponse;
import com.growit.common.response.IdDto;
import com.growit.goal.controller.dto.request.CreateGoalRequest;
import com.growit.goal.controller.mapper.GoalRequestMapper;
import com.growit.goal.domain.goal.Goal;
import com.growit.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.goal.domain.goal.dto.DeleteGoalCommand;
import com.growit.goal.usecase.CreateGoalUseCase;
import com.growit.goal.usecase.DeleteGoalUseCase;
import com.growit.goal.usecase.GetUserGoalsUseCase;
import com.growit.user.domain.user.User;
import jakarta.validation.Valid;
import java.rmi.ServerException;
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
      @PathVariable String id, @AuthenticationPrincipal User user) throws ServerException {
    DeleteGoalCommand command = goalRequestMapper.toDeleteCommand(id, user.getId());
    deleteGoalUseCase.execute(command);
    return ResponseEntity.ok(ApiResponse.success("삭제가 완료 되었습니다."));
  }
}
