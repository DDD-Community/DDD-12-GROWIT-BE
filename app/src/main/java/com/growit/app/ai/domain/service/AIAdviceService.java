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
    log.info("Saving AI advice for user: {}, adviceId: {}", event.getUserId(), event.getAdviceId());
    
    if (event.isSuccess() && event.getOutput() != null) {
      eventPublisher.publishEvent(event);
      log.info("Published AIAdviceResponseEvent for AIKPTS creation: adviceId={}", event.getAdviceId());
    }
  }

}
