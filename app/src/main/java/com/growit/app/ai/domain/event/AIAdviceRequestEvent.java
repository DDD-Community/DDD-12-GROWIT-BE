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
public class AIAdviceRequestEvent {
  
  @JsonProperty("eventType")
  @Builder.Default
  private String eventType = "AI_ADVICE_REQUEST";
  
  @JsonProperty("userId")
  private String userId;
  
  @JsonProperty("goalMentorId")
  private String goalMentorId;
  
  @JsonProperty("date")
  private LocalDate date;
  
  
  @JsonProperty("recentTodos")
  private List<String> recentTodos;
  
  @JsonProperty("weeklyRetrospects")
  private List<String> weeklyRetrospects;
  
  @JsonProperty("overallGoal")
  private String overallGoal;
  
  @JsonProperty("promptId")
  private String promptId;
  
  @JsonProperty("templateUid")
  private String templateUid;
}
