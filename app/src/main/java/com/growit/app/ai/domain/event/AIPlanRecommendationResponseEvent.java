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
public class AIPlanRecommendationResponseEvent {

    @JsonProperty("eventType")
    @Builder.Default
    private String eventType = "AI_PLAN_RECOMMENDATION_RESPONSE";

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("goalId")
    private String goalId;

    @JsonProperty("planId")
    private String planId;

    @JsonProperty("output")
    private String output;

    @JsonProperty("generatedAt")
    private LocalDateTime generatedAt;

    @JsonProperty("error")
    private String error;
}
