package com.growit.app.retrospect.controller.goalretrospect.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalWithGoalRetrospectResponse {

  private GoalDto goal;
  private GoalRetrospectDto goalRetrospect;

  @Getter
  @AllArgsConstructor
  public static class GoalDto {
    private String id;
    private String name;
    private DurationDto duration;

    @Getter
    @AllArgsConstructor
    public static class DurationDto {
      private LocalDate startDate;
      private LocalDate endDate;
    }
  }

  @Getter
  @AllArgsConstructor
  public static class GoalRetrospectDto {
    private String id;
    private boolean isCompleted;
  }
}
