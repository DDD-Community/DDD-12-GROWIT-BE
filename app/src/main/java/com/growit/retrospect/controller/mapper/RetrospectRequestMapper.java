package com.growit.retrospect.controller.mapper;

import com.growit.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.retrospect.domain.dto.CreateRetrospectCommand;
import org.springframework.stereotype.Component;

@Component
public class RetrospectRequestMapper {

  public CreateRetrospectCommand toCommand(String userId, CreateRetrospectRequest request) {
    return new CreateRetrospectCommand(
        request.getGoalId(), request.getPlanId(), userId, request.getContent());
  }
}
