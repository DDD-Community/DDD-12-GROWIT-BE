package com.growit.app.advice.controller;

import com.growit.app.advice.scheduler.MentorAdviceScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    mentorAdviceScheduler.generateDailyMentorAdvice();
    return ResponseEntity.ok("멘토 조언 생성 스케줄러가 수동으로 실행되었습니다.");
  }

  @PostMapping("/goal-recommendation")
  public ResponseEntity<String> runGoalRecommendationScheduler() {
    mentorAdviceScheduler.generateWeeklyGoalRecommendation();
    return ResponseEntity.ok("주간 목표 추천 생성 스케줄러가 수동으로 실행되었습니다.");
  }
}
