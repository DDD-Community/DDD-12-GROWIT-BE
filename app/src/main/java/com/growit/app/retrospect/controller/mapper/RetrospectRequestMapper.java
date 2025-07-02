package com.growit.app.retrospect.controller.mapper;

import com.growit.app.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;
import org.springframework.stereotype.Component;

@Component
public class RetrospectRequestMapper {

  public CreateRetrospectCommand toCommand(String userId, CreateRetrospectRequest request) {
    return new CreateRetrospectCommand(
        request.getGoalId(), request.getPlanId(), userId, request.getContent());
  }
}
