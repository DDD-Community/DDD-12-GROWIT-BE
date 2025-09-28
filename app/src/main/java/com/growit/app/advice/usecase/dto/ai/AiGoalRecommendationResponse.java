package com.growit.app.advice.usecase.dto.ai;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiGoalRecommendationResponse {
  private boolean success;
  private String userId;
  private String promptId;
  private String id;
  private String output;
  private ZonedDateTime generatedAt;
}
