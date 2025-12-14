package com.growit.app.goal.controller.mapper;

import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.domain.goal.dto.*;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import org.springframework.stereotype.Component;

@Component
public class GoalRequestMapper {

  public CreateGoalCommand toCommand(String userId, CreateGoalRequest request) {
    return new CreateGoalCommand(
        userId,
        request.getName(),
        new GoalDuration(request.getDuration().getStartDate(), request.getDuration().getEndDate()));
  }

  public UpdateGoalCommand toUpdateCommand(String id, String userId, CreateGoalRequest request) {
    return new UpdateGoalCommand(
        id,
        userId,
        request.getName(),
        new GoalDuration(request.getDuration().getStartDate(), request.getDuration().getEndDate()));
  }

  public DeleteGoalCommand toDeleteCommand(String id, String userId) {
    return new DeleteGoalCommand(id, userId);
  }

}
