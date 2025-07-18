package com.growit.app.retrospect.controller.mapper;

import com.growit.app.retrospect.controller.dto.response.RetrospectExistResponse;
import com.growit.app.retrospect.controller.dto.response.RetrospectResponse;
import com.growit.app.retrospect.usecase.dto.RetrospectWithPlan;
import org.springframework.stereotype.Component;

@Component
public class RetrospectResponseMapper {
  public RetrospectResponse toResponse(RetrospectWithPlan result) {
    return new RetrospectResponse(
        result.getRetrospect().getId(),
        result.getRetrospect().getGoalId(),
        result.getPlan(),
        result.getPlan().getContent());
  }

  public RetrospectExistResponse toExistResponse(boolean isExist) {
    return new RetrospectExistResponse(isExist);
  }
}
