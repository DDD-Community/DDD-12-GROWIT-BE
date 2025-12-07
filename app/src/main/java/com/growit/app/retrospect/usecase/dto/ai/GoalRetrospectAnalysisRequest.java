package com.growit.app.retrospect.usecase.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoalRetrospectAnalysisRequest {
  private String goalId;
  private String content;
  private GoalDto goal;
  private List<RetrospectDto> retrospects;
  private List<TodoDto> todos;

  @Getter
  @Builder
  public static class GoalDto {
    private String id;
    private String title;
    private String description;
  }

  @Getter
  @Builder
  public static class RetrospectDto {
    private String id;
    private String content;
    private KptDto kpt;
  }

  @Getter
  @Builder
  public static class KptDto {
    private String keep;
    private String problem;

    @JsonProperty("try")
    private String tryNext;
  }

  @Getter
  @Builder
  public static class TodoDto {
    private String id;
    private String title;
    private boolean completed;
  }
}


