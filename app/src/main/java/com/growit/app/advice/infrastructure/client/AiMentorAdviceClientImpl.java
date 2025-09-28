package com.growit.app.advice.infrastructure.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.advice.domain.mentor.service.AiMentorAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationRequest;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationResponse;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceResponse;
import javax.annotation.PostConstruct;
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

  @PostConstruct
  public void init() {
    if (nestApiUrl != null) {
      this.nestApiUrl = this.nestApiUrl.trim();
      log.info("AI Mentor Server URL 초기화: '{}'", nestApiUrl);
    } else {
      log.warn("AI Mentor Server URL이 설정되지 않았습니다.");
    }
  }

  @Override
  public AiMentorAdviceResponse getMentorAdvice(AiMentorAdviceRequest request) {
    String fullUrl = nestApiUrl + "/daily-advice";
    log.debug("AI Mentor Server 멘토 조언 요청 시작 - URL: {}", fullUrl);

    try {
      AiMentorAdviceResponse response =
          webClient
              .post()
              .uri(fullUrl)
              .bodyValue(request)
              .retrieve()
              .bodyToMono(AiMentorAdviceResponse.class)
              .block();

      log.debug("AI Mentor Server 멘토 조언 요청 성공");
      return response;
    } catch (Exception e) {
      log.error("AI Mentor Server 멘토 조언 요청 실패 - URL: {}, Error: {}", fullUrl, e.getMessage(), e);
      throw e;
    }
  }

  @Override
  public AiGoalRecommendationResponse getGoalRecommendation(AiGoalRecommendationRequest request) {
    String fullUrl = nestApiUrl + "/goal-recommendation";

    try {
      log.info(
          "AI Mentor Server 목표 추천 요청 시작 - URL: {}, Body: {}",
          fullUrl,
          objectMapper.writeValueAsString(request));
    } catch (JsonProcessingException e) {
      log.error("목표 추천 요청 JSON 직렬화 오류", e);
    }

    try {
      AiGoalRecommendationResponse response =
          webClient
              .post()
              .uri(fullUrl)
              .bodyValue(request)
              .retrieve()
              .bodyToMono(AiGoalRecommendationResponse.class)
              .block();

      log.debug("AI Mentor Server 목표 추천 요청 성공");
      return response;
    } catch (Exception e) {
      log.error("AI Mentor Server 목표 추천 요청 실패 - URL: {}, Error: {}", fullUrl, e.getMessage(), e);
      throw e;
    }
  }
}
