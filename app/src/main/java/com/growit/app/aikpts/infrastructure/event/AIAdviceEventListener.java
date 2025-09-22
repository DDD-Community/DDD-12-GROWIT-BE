package com.growit.app.aikpts.infrastructure.event;

import com.growit.app.ai.domain.event.AIAdviceResponseEvent;
import com.growit.app.aikpts.usecase.CreateAIKPTSFromAdviceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AIAdviceEventListener {
  
  private final CreateAIKPTSFromAdviceUseCase createAIKPTSFromAdviceUseCase;

  @EventListener
  public void handleAIAdviceResponse(AIAdviceResponseEvent event) {
    try {
      log.info("Processing AI advice response event: adviceId={}, userId={}", 
          event.getAdviceId(), event.getUserId());
      
      String aikptsId = createAIKPTSFromAdviceUseCase.execute(event);
      
      log.info("Successfully created AIKPTS: id={}, adviceId={}", 
          aikptsId, event.getAdviceId());
          
    } catch (Exception e) {
      log.error("Failed to create AIKPTS from advice event: adviceId={}, error={}", 
          event.getAdviceId(), e.getMessage(), e);
    }
  }
}
