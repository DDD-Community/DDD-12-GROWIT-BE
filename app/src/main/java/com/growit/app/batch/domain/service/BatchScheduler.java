package com.growit.app.batch.domain.service;

import com.growit.app.batch.domain.batchjob.BatchJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "batch.scheduler.enabled", havingValue = "true")
public class BatchScheduler {
  
  private final AIBatchService aiBatchService;

  /**
   * 매일 자정에 일일 조언 생성 배치 실행
   */
  @Scheduled(cron = "0 0 0 * * *")
  public void executeDailyAdviceGeneration() {
    log.info("Starting scheduled daily advice generation");
    
    try {
      BatchJob batchJob = aiBatchService.executeDailyAdviceGeneration();
      log.info("Scheduled daily advice generation completed: {}", batchJob);
    } catch (Exception e) {
      log.error("Scheduled daily advice generation failed", e);
    }
  }

  /**
   * 매주 월요일 자정에 주간 목표 추천 배치 실행
   */
  @Scheduled(cron = "0 0 0 * * MON")
  public void executeWeeklyPlanRecommendation() {
    log.info("Starting scheduled weekly plan recommendation");
    
    try {
      BatchJob batchJob = aiBatchService.executeWeeklyPlanRecommendation();
      log.info("Scheduled weekly plan recommendation completed: {}", batchJob);
    } catch (Exception e) {
      log.error("Scheduled weekly plan recommendation failed", e);
    }
  }
}
