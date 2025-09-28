package com.growit.app.advice.scheduler;

import com.growit.app.advice.domain.mentor.MentorAdviceRepository;
import com.growit.app.advice.usecase.GenerateMentorAdviceUseCase;
import com.growit.app.goal.usecase.GenerateGoalRecommendationUseCase;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorAdviceScheduler {

  private final GenerateMentorAdviceUseCase generateMentorAdviceUseCase;
  private final GenerateGoalRecommendationUseCase generateGoalRecommendationUseCase;
  private final MentorAdviceRepository mentorAdviceRepository;
  private final UserRepository userRepository;

  // 배치 실행 상태 추적
  private volatile boolean mentorAdviceRunning = false;
  private volatile boolean goalRecommendationRunning = false;
  private volatile String lastMentorAdviceResult = "대기 중";
  private volatile String lastGoalRecommendationResult = "대기 중";

  /** 매일 오전 0시에 오늘의 조언을 생성합니다. cron = "초 분 시 일 월 요일" */
  @Scheduled(cron = "0 0 0 * * *")
  public void generateDailyMentorAdvice() {
    generateDailyMentorAdviceAsync();
  }

  @Async
  public CompletableFuture<Void> generateDailyMentorAdviceAsync() {
    mentorAdviceRunning = true;
    lastMentorAdviceResult = "실행 중...";
    log.info("=== 데일리 멘토 조언 생성 스케줄러 시작 ===");

    int pageSize = 100;
    int pageNumber = 0;
    Page<User> userPage;
    int totalProcessed = 0;
    int successCount = 0;
    int skipCount = 0;
    int errorCount = 0;

    do {
      Pageable pageable = PageRequest.of(pageNumber, pageSize);
      userPage = userRepository.findAll(pageable);

      log.info(
          "페이지 {}/{} 처리 중 - 현재 페이지 사용자 수: {}",
          pageNumber + 1,
          userPage.getTotalPages(),
          userPage.getNumberOfElements());

      if (pageNumber == 0) {
        log.info("전체 사용자 수: {}", userPage.getTotalElements());
      }

      for (User user : userPage.getContent()) {
        totalProcessed++;
        try {
          var mentorAdviceOptional = generateMentorAdviceUseCase.tryExecute(user);
          if (mentorAdviceOptional.isPresent()) {
            mentorAdviceRepository.save(mentorAdviceOptional.get());
            successCount++;
          } else {
            skipCount++; // 진행중인 목표가 없어서 스킵
          }
        } catch (Exception e) {
          errorCount++; // AI 서버 오류 등
        }
      }

      log.info(
          "페이지 {} 처리 완료 - 성공: {}, 스킵: {}, 실패: {}, 진행률: {}/{}",
          pageNumber + 1,
          successCount,
          skipCount,
          errorCount,
          totalProcessed,
          userPage.getTotalElements());

      pageNumber++;
    } while (userPage.hasNext());

    log.info("=== 데일리 멘토 조언 생성 스케줄러 종료 ===");
    log.info(
        "최종 결과 - 전체: {}, 성공: {}, 스킵: {}, 실패: {}",
        totalProcessed,
        successCount,
        skipCount,
        errorCount);

    // 상태 업데이트
    lastMentorAdviceResult =
        String.format(
            "완료 - 전체: %d, 성공: %d, 스킵: %d, 실패: %d",
            totalProcessed, successCount, skipCount, errorCount);
    mentorAdviceRunning = false;

    return CompletableFuture.completedFuture(null);
  }

  /** 매주 월요일 오전 0시에 주간 목표 추천을 생성합니다. */
  @Scheduled(cron = "0 0 0 * * MON")
  public void generateWeeklyGoalRecommendation() {
    generateWeeklyGoalRecommendationAsync();
  }

  @Async
  public CompletableFuture<Void> generateWeeklyGoalRecommendationAsync() {
    goalRecommendationRunning = true;
    lastGoalRecommendationResult = "실행 중...";
    log.info("=== 주간 목표 추천 생성 스케줄러 시작 ===");

    int pageSize = 100;
    int pageNumber = 0;
    Page<User> userPage;
    int totalProcessed = 0;
    int successCount = 0;
    int skipCount = 0;
    int errorCount = 0;

    do {
      Pageable pageable = PageRequest.of(pageNumber, pageSize);
      userPage = userRepository.findAll(pageable);

      log.info(
          "페이지 {}/{} 처리 중 - 현재 페이지 사용자 수: {}",
          pageNumber + 1,
          userPage.getTotalPages(),
          userPage.getNumberOfElements());

      if (pageNumber == 0) {
        log.info("전체 사용자 수: {}", userPage.getTotalElements());
      }

      for (User user : userPage.getContent()) {
        totalProcessed++;
        try {
          var recommendationOptional = generateGoalRecommendationUseCase.tryExecute(user);
          if (recommendationOptional.isPresent()) {
            successCount++;
          } else {
            skipCount++; // 진행중인 목표가 없어서 스킵
          }
        } catch (Exception e) {
          errorCount++; // AI 서버 오류 등
        }
      }

      log.info(
          "페이지 {} 처리 완료 - 성공: {}, 스킵: {}, 실패: {}, 진행률: {}/{}",
          pageNumber + 1,
          successCount,
          skipCount,
          errorCount,
          totalProcessed,
          userPage.getTotalElements());

      pageNumber++;
    } while (userPage.hasNext());

    log.info("=== 주간 목표 추천 생성 스케줄러 종료 ===");
    log.info(
        "최종 결과  - 전체: {}, 성공: {}, 스킵: {}, 실패: {}",
        totalProcessed,
        successCount,
        skipCount,
        errorCount);

    // 상태 업데이트
    lastGoalRecommendationResult =
        String.format(
            "완료 - 전체: %d, 성공: %d, 스킵: %d, 실패: %d",
            totalProcessed, successCount, skipCount, errorCount);
    goalRecommendationRunning = false;

    return CompletableFuture.completedFuture(null);
  }

  // 상태 조회 메서드들
  public boolean isMentorAdviceRunning() {
    return mentorAdviceRunning;
  }

  public boolean isGoalRecommendationRunning() {
    return goalRecommendationRunning;
  }

  public String getLastMentorAdviceResult() {
    return lastMentorAdviceResult;
  }

  public String getLastGoalRecommendationResult() {
    return lastGoalRecommendationResult;
  }
}
