package com.growit.app.goal.controller.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalResponse {
  private String id;
  private String name;
  private DurationResponse duration;
  private BeforeAfterResponse beforeAfter;
  private List<PlanResponse> plans;
}
