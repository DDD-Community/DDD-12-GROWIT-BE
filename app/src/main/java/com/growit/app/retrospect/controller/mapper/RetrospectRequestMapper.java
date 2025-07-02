package com.growit.app.retrospect.controller.mapper;

import com.growit.app.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.domain.retrospect.command.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.command.UpdateRetrospectCommand;
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
}
