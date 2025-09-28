package com.growit.app.advice.controller;

import com.growit.app.advice.scheduler.MentorAdviceScheduler;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
    runMentorAdviceAsync();
    return ResponseEntity.ok("멘토 조언 생성 스케줄러가 백그라운드에서 시작되었습니다. 로그를 확인하여 진행 상황을 모니터링하세요.");
  }

  @Async
  public CompletableFuture<Void> runMentorAdviceAsync() {
    mentorAdviceScheduler.generateDailyMentorAdviceAsync();
    return CompletableFuture.completedFuture(null);
  }

  @PostMapping("/goal-recommendation")
  public ResponseEntity<String> runGoalRecommendationScheduler() {
    runGoalRecommendationAsync();
    return ResponseEntity.ok("주간 목표 추천 생성 스케줄러가 백그라운드에서 시작되었습니다. 로그를 확인하여 진행 상황을 모니터링하세요.");
  }

  @Async
  public CompletableFuture<Void> runGoalRecommendationAsync() {
    mentorAdviceScheduler.generateWeeklyGoalRecommendationAsync();
    return CompletableFuture.completedFuture(null);
  }
}
