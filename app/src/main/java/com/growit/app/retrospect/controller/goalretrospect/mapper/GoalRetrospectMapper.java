package com.growit.app.retrospect.controller.goalretrospect.mapper;

import com.growit.app.retrospect.controller.goalretrospect.dto.request.CreateGoalRetrospectRequest;
import com.growit.app.retrospect.controller.goalretrospect.dto.request.UpdateGoalRetrospectRequest;
import com.growit.app.retrospect.controller.goalretrospect.dto.response.GoalRetrospectResponse;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.dto.CreateGoalRetrospectCommand;
import com.growit.app.retrospect.domain.goalretrospect.dto.UpdateGoalRetrospectCommand;
import org.springframework.stereotype.Component;

@Component
public class GoalRetrospectMapper {

  public CreateGoalRetrospectCommand toCreateCommand(
      String userId, CreateGoalRetrospectRequest request) {
    return new CreateGoalRetrospectCommand(userId, request.getGoalId());
  }

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
