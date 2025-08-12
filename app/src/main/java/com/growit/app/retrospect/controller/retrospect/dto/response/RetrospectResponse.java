package com.growit.app.retrospect.controller.retrospect.dto.response;

import com.growit.app.goal.domain.goal.plan.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RetrospectResponse {
  private String id;
  private String goalId;
  private Plan plan;
  private String content;
}
