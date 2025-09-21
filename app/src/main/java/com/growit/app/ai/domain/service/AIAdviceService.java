package com.growit.app.ai.domain.service;

import com.growit.app.ai.domain.event.AIAdviceResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIAdviceService {

  public void saveAIAdvice(AIAdviceResponseEvent event) {
    // AI 조언 결과를 데이터베이스에 저장
    // TODO: AIAdvice 엔티티 구현 후 저장 로직 추가
    log.info("Saving AI advice for user: {}, adviceId: {}", event.getUserId(), event.getAdviceId());
  }

  public void handleAdviceGenerationFailure(AIAdviceResponseEvent event) {
    // AI 조언 생성 실패 시 이전 데이터 사용 또는 에러 처리
    log.error("Handling advice generation failure for user: {}, error: {}", 
             event.getUserId(), event.getError());
  }
}
