package com.growit.app.goal.controller.goalretrospect.mapper;

import com.growit.app.goal.controller.goalretrospect.dto.UpdateGoalRetrospectRequest;
import com.growit.app.retrospect.domain.goalretrospect.dto.UpdateGoalRetrospectCommand;
import org.springframework.stereotype.Component;

@Component
public class GoalRetrospectMapper {

  public UpdateGoalRetrospectCommand toUpdateCommand(
      String id, String userId, UpdateGoalRetrospectRequest request) {
    return new UpdateGoalRetrospectCommand(id, userId, request.getContent());
  }
}
