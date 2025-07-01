package com.growit.app.goal.controller.mapper;

import com.growit.app.goal.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.goal.domain.retrospect.dto.CreateRetrospectCommand;
import org.springframework.stereotype.Component;

@Component
public class RetrospectRequestMapper {

  public CreateRetrospectCommand toCommand(String userId, CreateRetrospectRequest request) {
    return new CreateRetrospectCommand(
        request.getGoalId(),
        request.getPlanId(),
        userId,
        request.getContent());
  }
}