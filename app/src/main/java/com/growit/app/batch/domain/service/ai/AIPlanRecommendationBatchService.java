package com.growit.app.batch.domain.service.ai;

import com.growit.app.ai.domain.event.AIPlanRecommendationRequestEvent;
import com.growit.app.ai.domain.event.AIPlanRecommendationResponseEvent;
import com.growit.app.ai.domain.service.AIDataService;
import com.growit.app.ai.infrastructure.client.AIServiceClient;
import com.growit.app.ai.infrastructure.event.EventPublisher;
import com.growit.app.batch.domain.batchjob.BatchJob;
import com.growit.app.batch.domain.batchjob.vo.BatchJobStatus;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIPlanRecommendationBatchService {
  
  private final EventPublisher eventPublisher;
  private final AIServiceClient aiServiceClient;
  private final AIDataService aiDataService;
  private final GoalRepository goalRepository;

  public BatchJob executeWeeklyPlanRecommendation() {
    BatchJob batchJob = BatchJob.builder()
        .id("weekly-plan-" + System.currentTimeMillis())
        .jobName("주간 목표 추천")
        .status(BatchJobStatus.PENDING)
        .build();
    
    batchJob.start();
    
    try {
      log.info("Starting weekly plan recommendation batch job");
      
      // 진행중인 목표 조회
      List<Goal> activeGoals = goalRepository.findActiveGoals();
      
      int processed = 0;
      int success = 0;
      int failure = 0;
      
      for (Goal goal : activeGoals) {
        try {
          AIPlanRecommendationRequestEvent requestEvent = createPlanRecommendationEvent(goal);
          
          AIPlanRecommendationResponseEvent responseEvent = aiServiceClient.generatePlanRecommendation(requestEvent);
          
          if (responseEvent.isSuccess()) {
            eventPublisher.publishPlanRecommendationRequest(requestEvent);
            success++;
          } else {
            log.error("AI plan recommendation failed for goal: {}, error: {}", 
                     goal.getId(), responseEvent.getError());
            failure++;
          }
          processed++;
          
        } catch (Exception e) {
          log.error("Failed to generate plan recommendation for goal: {}", goal.getId(), e);
          failure++;
        }
      }
      
      batchJob.complete(processed, success, failure);
      log.info("Weekly plan recommendation completed. Processed: {}, Success: {}, Failure: {}", 
               processed, success, failure);
      
    } catch (Exception e) {
      log.error("Weekly plan recommendation batch job failed", e);
      batchJob.fail(e.getMessage());
    }
    
    return batchJob;
  }

  private AIPlanRecommendationRequestEvent createPlanRecommendationEvent(Goal goal) {
    AIDataService.AIDataResponse data = aiDataService.getDataForPlanRecommendation(goal.getUserId(), goal.getId());
    
    return AIPlanRecommendationRequestEvent.builder()
        .userId(goal.getUserId())
        .goalId(goal.getId())
        .planId("")
        .date(LocalDate.now())
        .goalCategory(goal.getCategory().name())
        .goalName(goal.getName())
        .toBe(goal.getToBe())
        .recentProgress(data.getRecentTodos())
        .promptId("teamcook-goal-001")
        .templateUid("default-template")
        .build();
  }
}
