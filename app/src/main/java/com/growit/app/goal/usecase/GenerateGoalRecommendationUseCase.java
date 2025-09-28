package com.growit.app.goal.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.goalrecommendation.service.GoalRecommendationDataCollector;
import com.growit.app.goal.domain.goalrecommendation.service.GoalRecommendationService;
import com.growit.app.goal.domain.goalrecommendation.vo.GoalRecommendationData;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendation;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendationRepository;
import com.growit.app.user.domain.user.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 목표 추천 생성 UseCase - 진행 중인 목표 확인 - 추천 데이터 수집 - AI 추천 생성 - 추천 저장 */
@Service
@RequiredArgsConstructor
@Transactional
public class GenerateGoalRecommendationUseCase {

  private final GetUserGoalsUseCase getUserGoalsUseCase;
  private final GoalRecommendationDataCollector dataCollector;
  private final GoalRecommendationService recommendationService;
  private final PlanRecommendationRepository planRecommendationRepository;

  /**
   * 목표 추천을 생성합니다.
   *
   * @param user 사용자
   * @return 생성된 계획 추천
   * @throws NotFoundException 진행 중인 목표가 없는 경우
   */
  public PlanRecommendation execute(User user) {
    Goal currentGoal = getCurrentProgressGoal(user);
    GoalRecommendationData data = dataCollector.collectData(user, currentGoal);
    PlanRecommendation recommendation =
        recommendationService.generateRecommendation(user, currentGoal, data);

    planRecommendationRepository.save(recommendation);

    return recommendation;
  }

  /**
   * 목표 추천 생성을 시도합니다. 조건이 맞지 않으면 조용히 스킵합니다.
   *
   * @param user 사용자
   * @return 생성된 계획 추천 (조건이 맞지 않으면 Optional.empty())
   */
  public Optional<PlanRecommendation> tryExecute(User user) {
    Optional<Goal> currentGoal = getCurrentProgressGoalOptional(user);

    if (currentGoal.isEmpty()) {
      return Optional.empty(); // 진행중인 목표 없음
    }

    Goal goal = currentGoal.get();

    // 플랜 체크
    if (goal.getPlans() == null || goal.getPlans().isEmpty()) {
      return Optional.empty(); // 플랜 없음 - 스킵
    }

    // 멘토 정보 체크
    if (goal.getMentor() == null) {
      return Optional.empty(); // 멘토 정보 없음 - 스킵
    }

    // Goal 엔티티의 mentor 값 검증 (실제로는 항상 유효함)
    try {
      validateMentorForGoalRecommendation(goal);
    } catch (IllegalArgumentException e) {
      return Optional.empty(); // 알 수 없는 멘토 - 스킵
    }

    try {
      GoalRecommendationData data = dataCollector.collectData(user, goal);
      PlanRecommendation recommendation =
          recommendationService.generateRecommendation(user, goal, data);
      planRecommendationRepository.save(recommendation);
      return Optional.of(recommendation);
    } catch (Exception e) {
      // AI 서버 오류 등은 실제 에러로 전파
      throw e;
    }
  }

  private Goal getCurrentProgressGoal(User user) {
    return getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream()
        .findFirst()
        .orElseThrow(() -> new NotFoundException("진행중인 목표가 없습니다."));
  }

  private Optional<Goal> getCurrentProgressGoalOptional(User user) {
    return getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream().findFirst();
  }

  /** Goal의 mentor 정보가 목표 추천 생성에 유효한지 검증합니다. */
  private void validateMentorForGoalRecommendation(Goal goal) {
    if (goal.getMentor() == null) {
      throw new IllegalArgumentException("목표에 멘토 정보가 설정되지 않았습니다.");
    }

    // Goal 엔티티의 mentor 값 검증
    switch (goal.getMentor()) {
      case TIM_COOK, WARREN_BUFFETT, CONFUCIUS -> {
        // 유효한 멘토
      }
      default -> throw new IllegalArgumentException("알 수 없는 멘토입니다: " + goal.getMentor());
    }
  }
}
