package com.growit.app.retrospect.usecase.dto.ai;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoalRetrospectAnalysisResponse {
  private boolean success;
  private String goalId;
  private int todoCompletedRate;
  private AnalysisDto analysis;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class AnalysisDto {
    private String summary;
    private String advice;
  }
}
