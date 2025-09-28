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
   * 목표 추천 생성을 시도합니다. 진행 중인 목표가 없으면 조용히 스킵합니다.
   *
   * @param user 사용자
   * @return 생성된 계획 추천 (진행 중인 목표가 없으면 Optional.empty())
   */
  public Optional<PlanRecommendation> tryExecute(User user) {
    Optional<Goal> currentGoal = getCurrentProgressGoalOptional(user);

    if (currentGoal.isEmpty()) {
      return Optional.empty();
    }

    GoalRecommendationData data = dataCollector.collectData(user, currentGoal.get());
    PlanRecommendation recommendation =
        recommendationService.generateRecommendation(user, currentGoal.get(), data);

    planRecommendationRepository.save(recommendation);

    return Optional.of(recommendation);
  }

  private Goal getCurrentProgressGoal(User user) {
    return getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream()
        .findFirst()
        .orElseThrow(() -> new NotFoundException("진행중인 목표가 없습니다."));
  }

  private Optional<Goal> getCurrentProgressGoalOptional(User user) {
    return getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream().findFirst();
  }
}
