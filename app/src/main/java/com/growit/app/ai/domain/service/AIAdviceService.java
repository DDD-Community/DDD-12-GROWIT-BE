package com.growit.app.ai.domain.service;

import com.growit.app.ai.domain.event.AIAdviceResponseEvent;
import com.growit.app.ai.domain.aiadvice.AIAdvice;
import com.growit.app.ai.domain.aiadvice.AIAdviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIAdviceService {

  private final ApplicationEventPublisher eventPublisher;
  private final AIAdviceRepository aiAdviceRepository;

  public void saveAIAdvice(AIAdviceResponseEvent event) {
    log.info("Saving AI advice for user: {}, adviceId: {}", event.getUserId(), event.getAdviceId());
    
    // AI 조언을 데이터베이스에 저장
    String adviceContent = event.isSuccess() && event.getOutput() != null ? 
        event.getOutput().getAdvice() : null;
    
    // 기존 AI 조언이 있는지 확인 (사용자당 하나)
    List<AIAdvice> existingAdvices = aiAdviceRepository.findByUserId(event.getUserId());
    Optional<AIAdvice> existingAdvice = existingAdvices.isEmpty() ? 
        Optional.empty() : Optional.of(existingAdvices.get(0));
    
    AIAdvice aiAdvice;
    if (existingAdvice.isPresent()) {
      // 기존 조언 업데이트
      AIAdvice existing = existingAdvice.get();
      aiAdvice = AIAdvice.builder()
          .id(existing.getId())  // 기존 ID 유지
          .userId(event.getUserId())
          .goalId(event.getGoalId())
          .promptId(event.getPromptId())
          .templateUid(event.getTemplateUid())
          .success(event.isSuccess())
          .errorMessage(event.getError())
          .adviceContent(adviceContent)
          .createdAt(existing.getCreatedAt())  // 기존 생성일 유지
          .updatedAt(LocalDateTime.now())      // 업데이트 시간 갱신
          .isDeleted(false)
          .build();
      log.info("Updating existing AI advice: adviceId={}", existing.getId());
    } else {
      // 새로운 조언 생성
      aiAdvice = AIAdvice.fromEvent(
          event.getAdviceId(),
          event.getUserId(),
          event.getGoalId(),
          event.getPromptId(),
          event.getTemplateUid(),
          event.isSuccess(),
          event.getError(),
          adviceContent
      );
      log.info("Creating new AI advice: adviceId={}", event.getAdviceId());
    }
    
    aiAdviceRepository.save(aiAdvice);
    log.info("Saved AI advice to database: adviceId={}", aiAdvice.getId());
    
    // AIKPTS 생성을 위한 이벤트 발행
    if (event.isSuccess() && event.getOutput() != null) {
      eventPublisher.publishEvent(event);
      log.info("Published AIAdviceResponseEvent for AIKPTS creation: adviceId={}", event.getAdviceId());
    }
  }

}
