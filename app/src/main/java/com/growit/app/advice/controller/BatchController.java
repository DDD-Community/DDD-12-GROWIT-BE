package com.growit.app.advice.controller;

import com.growit.app.advice.scheduler.MentorAdviceScheduler;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {

  private final MentorAdviceScheduler mentorAdviceScheduler;

  @PostMapping("/mentor-advice")
  public ResponseEntity<String> runMentorAdviceScheduler() {
    // 진짜 비동기 실행 - Spring AOP 문제 해결
    CompletableFuture.runAsync(() -> {
      mentorAdviceScheduler.generateDailyMentorAdviceAsync();
    });
    
    return ResponseEntity.ok("멘토 조언 생성 스케줄러가 백그라운드에서 시작되었습니다. 로그를 확인하여 진행 상황을 모니터링하세요.");
  }

  @PostMapping("/goal-recommendation")
  public ResponseEntity<String> runGoalRecommendationScheduler() {
    // 진짜 비동기 실행 - Spring AOP 문제 해결
    CompletableFuture.runAsync(() -> {
      mentorAdviceScheduler.generateWeeklyGoalRecommendationAsync();
    });
    
    return ResponseEntity.ok("주간 목표 추천 생성 스케줄러가 백그라운드에서 시작되었습니다. 로그를 확인하여 진행 상황을 모니터링하세요.");
  }

  @GetMapping("/status")
  public ResponseEntity<Map<String, Object>> getSchedulerStatus() {
    Map<String, Object> status = Map.of(
        "mentorAdviceRunning", mentorAdviceScheduler.isMentorAdviceRunning(),
        "lastMentorAdviceResult", mentorAdviceScheduler.getLastMentorAdviceResult(),
        "goalRecommendationRunning", mentorAdviceScheduler.isGoalRecommendationRunning(),
        "lastGoalRecommendationResult", mentorAdviceScheduler.getLastGoalRecommendationResult()
    );
    return ResponseEntity.ok(status);
  }
}
