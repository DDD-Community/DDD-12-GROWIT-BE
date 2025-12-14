package com.growit.app.goal.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.domain.goalrecommendation.service.GoalRecommendationDataCollector;
import com.growit.app.goal.domain.goalrecommendation.service.GoalRecommendationService;
import com.growit.app.goal.domain.goalrecommendation.vo.GoalRecommendationData;
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

  /**
   * 목표 추천을 생성합니다.
   *
   * @param user 사용자
   * @throws NotFoundException 진행 중인 목표가 없는 경우
   */
  public void execute(User user) {
    Goal currentGoal = getCurrentProgressGoal(user);
    GoalRecommendationData data = dataCollector.collectData(user, currentGoal);
    recommendationService.generateRecommendation(user, currentGoal, data);
  }

  /**
   * 목표 추천 생성을 시도합니다. 조건이 맞지 않으면 조용히 스킵합니다.
   *
   * @param user 사용자
   */
  public void tryExecute(User user) {
    Optional<Goal> currentGoal = getCurrentProgressGoalOptional(user);

    if (currentGoal.isEmpty()) {
      return; // 진행중인 목표 없음
    }

    Goal goal = currentGoal.get();

    try {
      GoalRecommendationData data = dataCollector.collectData(user, goal);
      recommendationService.generateRecommendation(user, goal, data);
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
    // Mentor is no longer part of Goal domain, no validation needed
  }
}
