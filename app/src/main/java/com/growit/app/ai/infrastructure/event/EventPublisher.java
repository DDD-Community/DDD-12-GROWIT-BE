package com.growit.app.ai.infrastructure.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.ai.domain.event.AIAdviceRequestEvent;
import com.growit.app.ai.domain.event.AIPlanRecommendationRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 이벤트 발행자
 * 현재는 로깅만 하고, 나중에 EventBridge로 확장 가능
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {
  
  private final ObjectMapper objectMapper;

  public void publishAdviceRequest(AIAdviceRequestEvent event) {
    try {
      String eventJson = objectMapper.writeValueAsString(event);
      log.info("AI Advice Request event: {}", eventJson);
      
      // EventBridge 연동 시 실제 발행 로직 추가 예정
      
    } catch (Exception e) {
      log.error("Failed to publish AI Advice Request event", e);
    }
  }

  public void publishPlanRecommendationRequest(AIPlanRecommendationRequestEvent event) {
    try {
      String eventJson = objectMapper.writeValueAsString(event);
      log.info("AI Plan Recommendation Request event: {}", eventJson);
      
      // EventBridge 연동 시 실제 발행 로직 추가 예정
      
    } catch (Exception e) {
      log.error("Failed to publish AI Plan Recommendation Request event", e);
    }
  }
}
