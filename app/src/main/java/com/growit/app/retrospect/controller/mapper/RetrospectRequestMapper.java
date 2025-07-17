package com.growit.app.retrospect.controller.mapper;

import com.growit.app.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.domain.retrospect.dto.CheckRetrospectExistsQueryFilter;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.dto.GetRetrospectByGoalIdAndPlanIdQueryFilter;
import com.growit.app.retrospect.domain.retrospect.dto.GetRetrospectQueryFilter;
import com.growit.app.retrospect.domain.retrospect.dto.UpdateRetrospectCommand;
import org.springframework.stereotype.Component;

@Component
public class RetrospectRequestMapper {

  public CreateRetrospectCommand toCreateCommand(String userId, CreateRetrospectRequest request) {
    return new CreateRetrospectCommand(
        request.getGoalId(), request.getPlanId(), userId, request.getContent());
  }

  public UpdateRetrospectCommand toUpdateCommand(
      String id, String userId, UpdateRetrospectRequest request) {
    return new UpdateRetrospectCommand(id, userId, request.getContent());
  }

  public GetRetrospectQueryFilter toGetCommand(String id, String userId) {
    return new GetRetrospectQueryFilter(id, userId);
  }

  public CheckRetrospectExistsQueryFilter toCheckQuery(
      String userId, String goalId, String planId) {
    return new CheckRetrospectExistsQueryFilter(userId, goalId, planId);
  }

  public GetRetrospectByGoalIdAndPlanIdQueryFilter toGetByGoalIdAndPlanIdQuery(
      String userId, String goalId, String planId) {
    return new GetRetrospectByGoalIdAndPlanIdQueryFilter(goalId, planId, userId);
  }
}
