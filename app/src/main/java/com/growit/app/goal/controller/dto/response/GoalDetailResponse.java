package com.growit.app.goal.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalDetailResponse {
  private String id;
  private String name;
  private PlanetDto planet;
  private DurationDto duration;
  private String status;
  private AnalysisDto analysis;
  private boolean isChecked;

  @JsonProperty("isChecked")
  public boolean isChecked() {
    return isChecked;
  }

  @Getter
  @AllArgsConstructor
  public static class PlanetDto {
    private String name;
    private ImageDto image;
  }

  @Getter
  @AllArgsConstructor
  public static class ImageDto {
    private String done;
    private String progress;
  }

  @Getter
  @AllArgsConstructor
  public static class DurationDto {
    private String startDate;
    private String endDate;
  }

  @Getter
  @AllArgsConstructor
  public static class AnalysisDto {
    private int todoCompletedRate;
    private String summary;
  }
}
