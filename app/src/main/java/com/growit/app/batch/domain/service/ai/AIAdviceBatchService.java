package com.growit.app.batch.domain.service.ai;

import com.growit.app.ai.domain.event.AIAdviceRequestEvent;
import com.growit.app.ai.domain.event.AIAdviceResponseEvent;
import com.growit.app.ai.domain.service.AIAdviceService;
import com.growit.app.ai.infrastructure.client.AIServiceClient;
import com.growit.app.ai.infrastructure.event.EventPublisher;
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
  private final UserRepository userRepository;
  private final GoalRepository goalRepository;
  private final ToDoRepository toDoRepository;

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
          // 사용자별 목표-멘토 매핑 조회
          List<String> goalMentorIds = getGoalMentorIds(user.getId());
          
          for (String goalMentorId : goalMentorIds) {
            // AI 조언 요청 이벤트 생성
            AIAdviceRequestEvent requestEvent = createAdviceRequestEvent(user.getId(), goalMentorId);
            
            // AI 서비스 호출
            AIAdviceResponseEvent responseEvent = aiServiceClient.generateAdvice(requestEvent);
            
            if (responseEvent.isSuccess()) {
              // 성공 시 AIAdviceService를 통해 저장 및 이벤트 발행
              aiAdviceService.saveAIAdvice(responseEvent);
              success++;
            } else {
              // 실패 시 로그만 기록
              log.error("AI advice generation failed for user: {}, goalMentor: {}, error: {}", 
                       user.getId(), goalMentorId, responseEvent.getError());
              failure++;
            }
            processed++;
          }
          
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

  private AIAdviceRequestEvent createAdviceRequestEvent(String userId, String goalMentorId) {
    // 최근 투두 조회 (최근 3일)
    List<String> recentTodos = getRecentTodos(userId);
    
    // 주간 회고 조회 (최근 1주일)
    List<String> weeklyRetrospects = getWeeklyRetrospects(userId);
    
    // 전체 목표 조회
    String overallGoal = getOverallGoal(userId);
    
    return AIAdviceRequestEvent.builder()
        .userId(userId)
        .goalMentorId(goalMentorId)
        .date(LocalDate.now())
        .mentorType("피터 레벨스") // TODO: 실제 멘토 타입 조회
        .recentTodos(recentTodos)
        .weeklyRetrospects(weeklyRetrospects)
        .overallGoal(overallGoal)
        .promptId("RX2NiGJ2uPxBG9PTHkdlT")
        .templateUid("default-template")
        .build();
  }

  private List<String> getGoalMentorIds(String userId) {
    // TODO: 실제 목표-멘토 매핑 조회 로직 구현
    return List.of("goal-mentor-001");
  }

  private List<String> getRecentTodos(String userId) {
    // 최근 3일간의 투두 조회
    LocalDate today = LocalDate.now();
    List<String> todos = List.of();
    
    for (int i = 0; i < 3; i++) {
      LocalDate date = today.minusDays(i);
      List<ToDo> dayTodos = toDoRepository.findByUserIdAndDate(userId, date);
      todos = dayTodos.stream()
          .map(ToDo::getContent)
          .toList();
    }
    
    return todos;
  }

  private List<String> getWeeklyRetrospects(String userId) {
    // TODO: 주간 회고 조회 로직 구현
    return List.of("주간 회고 1", "주간 회고 2");
  }

  private String getOverallGoal(String userId) {
    // 사용자의 현재 목표 조회
    Optional<Goal> currentGoal = goalRepository.findByUserIdAndGoalDuration(userId, LocalDate.now());
    return currentGoal.map(Goal::getName).orElse("목표 없음");
  }
}
