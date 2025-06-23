package com.growit.app.goal.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalResponse {
  private String id;
  private String name;
  private DurationResponse durationResponse;
  private BeforeAfterResponse beforeAfterResponse;
  private List<PlanResponse> plansResponse;






}



