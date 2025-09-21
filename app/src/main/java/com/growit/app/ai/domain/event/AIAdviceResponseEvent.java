package com.growit.app.ai.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIAdviceResponseEvent {
  
  @JsonProperty("eventType")
  @Builder.Default
  private String eventType = "AI_ADVICE_RESPONSE";
  
  @JsonProperty("success")
  private boolean success;
  
  @JsonProperty("userId")
  private String userId;
  
  @JsonProperty("goalMentorId")
  private String goalMentorId;
  
  @JsonProperty("adviceId")
  private String adviceId;
  
  @JsonProperty("output")
  private AdviceOutput output;
  
  @JsonProperty("generatedAt")
  private LocalDateTime generatedAt;
  
  @JsonProperty("error")
  private String error;
  
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AdviceOutput {
    @JsonProperty("keep")
    private String keeps;
    
    @JsonProperty("problem")
    private String problems;
    
    @JsonProperty("try")
    private String trys;
  }
}
