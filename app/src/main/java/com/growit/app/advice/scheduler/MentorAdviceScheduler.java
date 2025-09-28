package com.growit.app.advice.scheduler;

import com.growit.app.advice.domain.mentor.MentorAdviceRepository;
import com.growit.app.advice.usecase.GenerateMentorAdviceUseCase;
import com.growit.app.goal.usecase.GenerateGoalRecommendationUseCase;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  /** 매일 오전 0시에 오늘의 조언을 생성합니다. cron = "초 분 시 일 월 요일" */
  @Scheduled(cron = "0 0 0 * * *")
  public void generateDailyMentorAdvice() {
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
  }

  /** 매주 월요일 오전 0시에 주간 목표 추천을 생성합니다. */
  @Scheduled(cron = "0 0 0 * * MON")
  public void generateWeeklyGoalRecommendation() {
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
        "최종 결과 - 전체: {}, 성공: {}, 스킵: {}, 실패: {}",
        totalProcessed,
        successCount,
        skipCount,
        errorCount);
  }
}
