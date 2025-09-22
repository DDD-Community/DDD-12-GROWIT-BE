package com.growit.app.aikpts.usecase;

import com.growit.app.ai.domain.event.AIAdviceResponseEvent;
import com.growit.app.aikpts.domain.aikpts.service.AIKPTSFromAdviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAIKPTSFromAdviceUseCase {
  
  private final AIKPTSFromAdviceService aikptsFromAdviceService;

  public String execute(AIAdviceResponseEvent adviceEvent) {
    return aikptsFromAdviceService.upsertAIKPTSFromAdvice(adviceEvent);
  }
}
