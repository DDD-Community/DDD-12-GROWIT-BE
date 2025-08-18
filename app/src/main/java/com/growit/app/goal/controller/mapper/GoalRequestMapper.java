package com.growit.app.goal.controller.mapper;

import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.dto.request.UpdatePlanRequest;
import com.growit.app.goal.domain.goal.dto.*;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import org.springframework.stereotype.Component;

@Component
public class GoalRequestMapper {

  public CreateGoalCommand toCommand(String userId, CreateGoalRequest request) {
    return new CreateGoalCommand(
        userId,
        request.getName(),
        new GoalDuration(request.getDuration().getStartDate(), request.getDuration().getEndDate()),
        request.getToBe(),
        request.getCategory(),
        request.getPlans().stream()
            .map(planRequest -> new PlanDto(planRequest.getWeekOfMonth(), planRequest.getContent()))
            .toList());
  }

  public UpdateGoalCommand toUpdateCommand(String id, String userId, CreateGoalRequest request) {
    return new UpdateGoalCommand(
        id,
        userId,
        request.getName(),
        new GoalDuration(request.getDuration().getStartDate(), request.getDuration().getEndDate()),
        request.getToBe(),
        request.getCategory(),
        request.getPlans().stream()
            .map(planRequest -> new PlanDto(planRequest.getWeekOfMonth(), planRequest.getContent()))
            .toList());
  }

  public DeleteGoalCommand toDeleteCommand(String id, String userId) {
    return new DeleteGoalCommand(id, userId);
  }

  public UpdatePlanCommand toUpdatePlanCommand(
      String goalId, String planId, String userId, UpdatePlanRequest request) {
    return new UpdatePlanCommand(goalId, planId, userId, request.getContent());
  }
}
