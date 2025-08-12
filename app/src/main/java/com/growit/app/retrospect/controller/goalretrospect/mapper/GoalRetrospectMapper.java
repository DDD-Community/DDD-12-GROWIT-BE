package com.growit.app.retrospect.controller.goalretrospect.mapper;

import com.growit.app.retrospect.controller.goalretrospect.dto.GoalRetrospectResponse;
import com.growit.app.retrospect.controller.goalretrospect.dto.UpdateGoalRetrospectRequest;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.dto.UpdateGoalRetrospectCommand;
import org.springframework.stereotype.Component;

@Component
public class GoalRetrospectMapper {

  public UpdateGoalRetrospectCommand toUpdateCommand(
      String id, String userId, UpdateGoalRetrospectRequest request) {
    return new UpdateGoalRetrospectCommand(id, userId, request.getContent());
  }

  public GoalRetrospectResponse toResponse(GoalRetrospect goalRetrospect) {
    return new GoalRetrospectResponse(
        goalRetrospect.getId(),
        goalRetrospect.getGoalId(),
        goalRetrospect.getTodoCompletedRate(),
        goalRetrospect.getAnalysis(),
        goalRetrospect.getContent());
  }
}
