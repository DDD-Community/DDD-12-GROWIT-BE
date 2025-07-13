package com.growit.app.retrospect.controller.mapper;

import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.retrospect.controller.dto.response.RetrospectExistResponse;
import com.growit.app.retrospect.controller.dto.response.RetrospectResponse;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import org.springframework.stereotype.Component;

@Component
public class RetrospectResponseMapper {
  public RetrospectResponse toResponse(Retrospect retrospect, Plan plan) {
    return new RetrospectResponse(
        retrospect.getId(), retrospect.getGoalId(), plan, retrospect.getContent());
  }

  public RetrospectExistResponse toExistResponse(boolean isExist) {
    return new RetrospectExistResponse(isExist);
  }
}
