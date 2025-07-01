package com.growit.goal.controller.mapper;

import com.growit.goal.controller.dto.request.CreateGoalRequest;
import com.growit.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.goal.domain.goal.dto.DeleteGoalCommand;
import com.growit.goal.domain.goal.dto.PlanDto;
import com.growit.goal.domain.goal.vo.BeforeAfter;
import com.growit.goal.domain.goal.vo.GoalDuration;
import org.springframework.stereotype.Component;

@Component
public class GoalRequestMapper {

  public CreateGoalCommand toCommand(String userId, CreateGoalRequest request) {
    return new CreateGoalCommand(
        userId,
        request.getName(),
        new GoalDuration(request.getDuration().getStartDate(), request.getDuration().getEndDate()),
        new BeforeAfter(request.getBeforeAfter().getAsIs(), request.getBeforeAfter().getToBe()),
        request.getPlans().stream()
            .map(planRequest -> new PlanDto(planRequest.getWeekOfMonth(), planRequest.getContent()))
            .toList());
  }

  public DeleteGoalCommand toDeleteCommand(String id, String userId) {
    return new DeleteGoalCommand(id, userId);
  }
}
