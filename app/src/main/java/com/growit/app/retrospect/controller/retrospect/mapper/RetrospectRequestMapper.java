package com.growit.app.retrospect.controller.retrospect.mapper;

import com.growit.app.retrospect.controller.retrospect.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.retrospect.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.domain.retrospect.dto.*;
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

  public RetrospectQueryFilter toRetrospectQueryFilter(
      String userId, String goalId, String planId) {
    return new RetrospectQueryFilter(goalId, planId, userId);
  }
}
