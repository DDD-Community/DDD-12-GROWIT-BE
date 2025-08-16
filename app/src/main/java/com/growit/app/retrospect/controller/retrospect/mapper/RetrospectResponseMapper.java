package com.growit.app.retrospect.controller.retrospect.mapper;

import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.retrospect.controller.retrospect.dto.response.RetrospectExistResponse;
import com.growit.app.retrospect.controller.retrospect.dto.response.RetrospectResponse;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.usecase.retrospect.dto.RetrospectWithPlan;
import org.springframework.stereotype.Component;

@Component
public class RetrospectResponseMapper {
  public RetrospectResponse toResponse(RetrospectWithPlan result) {
    return new RetrospectResponse(toDto(result.getPlan()), toDto(result.getRetrospect()));
  }

  public RetrospectResponse.PlanDto toDto(Plan plan) {
    return new RetrospectResponse.PlanDto(
        plan.getId(), plan.getWeekOfMonth(), plan.isCurrentWeek(), plan.getContent());
  }

  public RetrospectResponse.RetrospectDto toDto(Retrospect retrospect) {
    return retrospect == null
        ? null
        : new RetrospectResponse.RetrospectDto(retrospect.getId(), retrospect.getContent());
  }

  public RetrospectExistResponse toExistResponse(boolean isExist) {
    return new RetrospectExistResponse(isExist);
  }
}
