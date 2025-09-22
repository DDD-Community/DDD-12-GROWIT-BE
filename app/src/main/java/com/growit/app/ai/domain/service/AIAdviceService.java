package com.growit.app.ai.domain.service;

import com.growit.app.ai.domain.event.AIAdviceResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIAdviceService {

  private final ApplicationEventPublisher eventPublisher;

  public void saveAIAdvice(AIAdviceResponseEvent event) {
    // AI 조언 결과를 데이터베이스에 저장
    // TODO: AIAdvice 엔티티 구현 후 저장 로직 추가
    log.info("Saving AI advice for user: {}, adviceId: {}", event.getUserId(), event.getAdviceId());
    
    // AIKPTS 생성을 위한 이벤트 발행
    if (event.isSuccess() && event.getOutput() != null) {
      eventPublisher.publishEvent(event);
      log.info("Published AIAdviceResponseEvent for AIKPTS creation: adviceId={}", event.getAdviceId());
    }
  }

}
