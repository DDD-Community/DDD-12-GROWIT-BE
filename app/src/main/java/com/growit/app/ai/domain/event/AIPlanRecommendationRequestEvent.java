package com.growit.app.ai.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIPlanRecommendationRequestEvent {
  
  @JsonProperty("eventType")
  @Builder.Default
  private String eventType = "AI_PLAN_RECOMMENDATION_REQUEST";
  
  @JsonProperty("userId")
  private String userId;
  
  @JsonProperty("goalId")
  private String goalId;
  
  @JsonProperty("planId")
  private String planId;
  
  @JsonProperty("date")
  private LocalDate date;
  
  
  @JsonProperty("goalCategory")
  private String goalCategory;
  
  @JsonProperty("goalName")
  private String goalName;
  
  @JsonProperty("toBe")
  private String toBe;
  
  @JsonProperty("recentProgress")
  private List<String> recentProgress;
  
  @JsonProperty("promptId")
  private String promptId;
  
  @JsonProperty("templateUid")
  private String templateUid;
}
