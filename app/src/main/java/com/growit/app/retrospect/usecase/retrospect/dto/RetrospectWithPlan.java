package com.growit.app.retrospect.usecase.retrospect.dto;

import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RetrospectWithPlan {
  private Retrospect retrospect;
  private Plan plan;
}
