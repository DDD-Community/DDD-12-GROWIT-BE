package com.growit.app.retrospect.controller.retrospect.mapper;

import com.growit.app.retrospect.controller.retrospect.dto.response.RetrospectExistResponse;
import com.growit.app.retrospect.controller.retrospect.dto.response.RetrospectResponse;
import com.growit.app.retrospect.usecase.retrospect.dto.RetrospectWithPlan;
import org.springframework.stereotype.Component;

@Component
public class RetrospectResponseMapper {
  public RetrospectResponse toResponse(RetrospectWithPlan result) {
    return new RetrospectResponse(
        result.getRetrospect().getId(),
        result.getRetrospect().getGoalId(),
        result.getPlan(),
        result.getRetrospect().getContent());
  }

  public RetrospectExistResponse toExistResponse(boolean isExist) {
    return new RetrospectExistResponse(isExist);
  }
}
