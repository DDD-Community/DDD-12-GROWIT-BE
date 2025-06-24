package com.growit.app.goal.controller.mapper;

import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
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
          .map(planRequest -> new PlanDto(planRequest.getContent()))
          .toList());
  }
}
