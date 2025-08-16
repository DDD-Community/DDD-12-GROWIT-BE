package com.growit.app.retrospect.controller.goalretrospect;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.IdDto;
import com.growit.app.retrospect.controller.goalretrospect.dto.request.CreateGoalRetrospectRequest;
import com.growit.app.retrospect.controller.goalretrospect.dto.request.UpdateGoalRetrospectRequest;
import com.growit.app.retrospect.controller.goalretrospect.dto.response.GoalRetrospectResponse;
import com.growit.app.retrospect.controller.goalretrospect.mapper.GoalRetrospectMapper;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.dto.CreateGoalRetrospectCommand;
import com.growit.app.retrospect.domain.goalretrospect.dto.UpdateGoalRetrospectCommand;
import com.growit.app.retrospect.usecase.goalretrospect.CreateGoalRetrospectUseCase;
import com.growit.app.retrospect.usecase.goalretrospect.GetGoalRetrospectUseCase;
import com.growit.app.retrospect.usecase.goalretrospect.UpdateGoalRetrospectUseCase;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goal-retrospects")
@RequiredArgsConstructor
public class GoalRetrospectController {
  private final CreateGoalRetrospectUseCase createGoalRetrospectUseCase;
  private final UpdateGoalRetrospectUseCase updateGoalRetrospectUseCase;
  private final GetGoalRetrospectUseCase getGoalRetrospectUseCase;
  private final GoalRetrospectMapper goalRetrospectMapper;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<GoalRetrospectResponse>> getGoalRetrospect(
      @AuthenticationPrincipal User user, @PathVariable String id) {
    GoalRetrospect goalRetrospect = getGoalRetrospectUseCase.execute(id, user.getId());
    GoalRetrospectResponse response = goalRetrospectMapper.toResponse(goalRetrospect);

    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<IdDto>> createGoalRetrospect(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateGoalRetrospectRequest request) {
    final CreateGoalRetrospectCommand command =
        goalRetrospectMapper.toCreateCommand(user.getId(), request);
    final String id = createGoalRetrospectUseCase.execute(command);

    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(new IdDto(id)));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> updateGoalRetrospect(
      @AuthenticationPrincipal User user,
      @PathVariable String id,
      @Valid @RequestBody UpdateGoalRetrospectRequest request) {
    UpdateGoalRetrospectCommand command =
        goalRetrospectMapper.toUpdateCommand(id, user.getId(), request);
    updateGoalRetrospectUseCase.execute(command);

    return ResponseEntity.ok().build();
  }
}
