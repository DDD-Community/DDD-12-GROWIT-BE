package com.growit.app.goal.controller.goalretrospect;

import com.growit.app.goal.controller.goalretrospect.dto.UpdateGoalRetrospectRequest;
import com.growit.app.goal.controller.goalretrospect.mapper.GoalRetrospectMapper;
import com.growit.app.retrospect.domain.goalretrospect.dto.UpdateGoalRetrospectCommand;
import com.growit.app.retrospect.usecase.UpdateGoalRetrospectUseCase;
import com.growit.app.user.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goal-retrospects")
@RequiredArgsConstructor
public class GoalRetrospectController {
  private final UpdateGoalRetrospectUseCase updateGoalRetrospectUseCase;
  private final GoalRetrospectMapper goalRetrospectMapper;

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