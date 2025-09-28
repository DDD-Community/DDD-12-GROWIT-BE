package com.growit.app.advice.infrastructure.client;

import com.growit.app.advice.domain.mentor.service.AiMentorAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationRequest;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationResponse;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiMentorAdviceClientImpl implements AiMentorAdviceClient {

  private final WebClient webClient;
  private final ObjectMapper objectMapper;

  @Value("${ai.mentor.url}")
  private String nestApiUrl;

  @Override
  public AiMentorAdviceResponse getMentorAdvice(AiMentorAdviceRequest request) {
    return webClient
        .post()
        .uri(nestApiUrl + "/daily-advice")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(AiMentorAdviceResponse.class)
        .block();
  }

  @Override
  public AiGoalRecommendationResponse getGoalRecommendation(AiGoalRecommendationRequest request) {
    try {
      log.info(
          "Sending goal recommendation request to AI Mentor Server. URL: {}, Body: {}",
          nestApiUrl + "/goal-recommendation",
          objectMapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      log.error("Error serializing AiGoalRecommendationRequest to JSON", e);
    }
    return webClient
        .post()
        .uri(nestApiUrl + "/goal-recommendation")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(AiGoalRecommendationResponse.class)
        .block();
  }
}
