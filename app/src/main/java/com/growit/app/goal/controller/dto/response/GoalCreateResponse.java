package com.growit.app.goal.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalCreateResponse {
  private String id;
  private PlanetDto planet;

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
}