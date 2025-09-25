package com.growit.app.retrospect.controller.retrospect.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RetrospectResponse {
  private PlanDto plan;
  private RetrospectDto retrospect;

  @Getter
  @AllArgsConstructor
  public static class RetrospectDto {
    private String id;
    private KptDto kpt;
  }

  @Getter
  @AllArgsConstructor
  public static class KptDto {
    private String keep;
    private String problem;
    private String tryNext;
  }

  @Getter
  @AllArgsConstructor
  public static class PlanDto {
    private String id;
    private int weekOfMonth;

    @JsonProperty("isCurrentWeek")
    private boolean currentWeek;

    private String content;
  }
}
