package com.growit.app.batch.domain.service.ai;

import com.growit.app.ai.domain.event.AIAdviceRequestEvent;
import com.growit.app.ai.domain.event.AIAdviceResponseEvent;
import com.growit.app.ai.domain.service.AIAdviceService;
import com.growit.app.ai.domain.service.AIDataService;
import com.growit.app.ai.infrastructure.client.AIServiceClient;
import com.growit.app.ai.infrastructure.event.EventPublisher;
import com.growit.app.ai.domain.aiadvice.AIAdviceRepository;
import com.growit.app.ai.domain.aiadvice.AIAdvice;
import com.growit.app.batch.domain.batchjob.BatchJob;
import com.growit.app.batch.domain.batchjob.vo.BatchJobStatus;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIAdviceBatchService {
  
  private final EventPublisher eventPublisher;
  private final AIServiceClient aiServiceClient;
  private final AIAdviceService aiAdviceService;
  private final AIDataService aiDataService;
  private final UserRepository userRepository;
  private final GoalRepository goalRepository;
  private final ToDoRepository toDoRepository;
  private final AIAdviceRepository aiAdviceRepository;

  public BatchJob executeDailyAdviceGeneration() {
    BatchJob batchJob = BatchJob.builder()
        .id("daily-advice-" + System.currentTimeMillis())
        .jobName("일일 조언 생성")
        .status(BatchJobStatus.PENDING)
        .build();
    
    batchJob.start();
    
    try {
      log.info("Starting daily advice generation batch job");
      
      // 활성 사용자 목록 조회
      List<User> activeUsers = userRepository.findActiveUsers();
      
      int processed = 0;
      int success = 0;
      int failure = 0;
      
      for (User user : activeUsers) {
        try {
          // 사용자의 활성 목표들을 가져와서 각 목표별로 AI 조언 생성
          List<Goal> userGoals = goalRepository.findByUserIdAndGoalDuration(user.getId(), LocalDate.now());
          
          if (userGoals.isEmpty()) {
            log.info("User {} has no active goals, skipping", user.getId());
            continue;
          }
          
          // 사용자당 하나의 AI 조언만 생성
          List<AIAdvice> existingAdvices = aiAdviceRepository.findByUserId(user.getId());
          boolean hasAdvice = !existingAdvices.isEmpty();
          
          if (hasAdvice) {
            log.info("User {} has existing advice, will update", user.getId());
          } else {
            log.info("User {} has no advice, will create new", user.getId());
          }
          
          // 첫 번째 진행중인 목표로 AI 조언 생성
          Goal firstGoal = userGoals.get(0);
          AIAdviceRequestEvent requestEvent = createAdviceRequestEvent(user.getId(), firstGoal.getId());
          
          AIAdviceResponseEvent responseEvent = aiServiceClient.generateAdvice(requestEvent);
          
          if (responseEvent.isSuccess()) {
            aiAdviceService.saveAIAdvice(responseEvent);
            success++;
          } else {
            log.error("AI advice generation failed for user: {}, goal: {}, error: {}", 
                     user.getId(), firstGoal.getId(), responseEvent.getError());
            failure++;
          }
          processed++;
          
        } catch (Exception e) {
          log.error("Failed to generate advice for user: {}", user.getId(), e);
          failure++;
        }
      }
      
      batchJob.complete(processed, success, failure);
      log.info("Daily advice generation completed. Processed: {}, Success: {}, Failure: {}", 
               processed, success, failure);
      
    } catch (Exception e) {
      log.error("Daily advice generation batch job failed", e);
      batchJob.fail(e.getMessage());
    }
    
    return batchJob;
  }

  private AIAdviceRequestEvent createAdviceRequestEvent(String userId, String goalId) {
    AIDataService.AIDataResponse data = aiDataService.getDataForAdvice(userId);
    
    return AIAdviceRequestEvent.builder()
        .userId(userId)
        .goalId(goalId)
        .goalMentorId("goal-mentor-001")
        .date(LocalDate.now())
        .recentTodos(data.getRecentTodos())
        .weeklyRetrospects(data.getWeeklyRetrospects())
        .overallGoal(data.getOverallGoal())
        .promptId("teamcook-advice-001")
        .templateUid("default-template")
        .build();
  }



}
