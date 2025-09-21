package com.growit.app.batch.domain.service;

import com.growit.app.batch.domain.batchjob.BatchJob;
import com.growit.app.batch.domain.batchjob.vo.BatchJobStatus;
import com.growit.app.batch.domain.service.ai.AIAdviceBatchService;
import com.growit.app.batch.domain.service.ai.AIPlanRecommendationBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIBatchService {
  
  private final AIAdviceBatchService aiAdviceBatchService;
  private final AIPlanRecommendationBatchService aiPlanRecommendationBatchService;

  public BatchJob executeDailyAdviceGeneration() {
    return aiAdviceBatchService.executeDailyAdviceGeneration();
  }

  public BatchJob executeWeeklyPlanRecommendation() {
    return aiPlanRecommendationBatchService.executeWeeklyPlanRecommendation();
  }
}
