package com.growit.app.advice.usecase;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.service.MentorAdviceDataCollector;
import com.growit.app.advice.domain.mentor.service.MentorAdviceService;
import com.growit.app.advice.domain.mentor.vo.MentorAdviceData;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.user.domain.user.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 멘토 조언 생성 UseCase - 진행 중인 목표 확인 - 조언 데이터 수집 - AI 조언 생성 */
@Service
@RequiredArgsConstructor
@Transactional
public class GenerateMentorAdviceUseCase {

  private final GetUserGoalsUseCase getUserGoalsUseCase;
  private final MentorAdviceDataCollector dataCollector;
  private final MentorAdviceService mentorAdviceService;

  /**
   * 멘토 조언을 생성합니다.
   *
   * @param user 사용자
   * @return 생성된 멘토 조언
   * @throws NotFoundException 진행 중인 목표가 없는 경우
   */
  public MentorAdvice execute(User user) {
    Goal currentGoal = getCurrentProgressGoal(user);
    MentorAdviceData data = dataCollector.collectData(user, currentGoal);

    return mentorAdviceService.generateAdvice(user, currentGoal, data);
  }

  /**
   * 멘토 조언 생성을 시도합니다. 조건이 맞지 않으면 조용히 스킵합니다.
   *
   * @param user 사용자
   * @return 생성된 멘토 조언 (조건이 맞지 않으면 Optional.empty())
   */
  public Optional<MentorAdvice> tryExecute(User user) {
    Optional<Goal> currentGoal = getCurrentProgressGoalOptional(user);
    if (currentGoal.isEmpty()) {
      return Optional.empty(); // 진행중인 목표 없음
    }

    Goal goal = currentGoal.get();
    // 멘토 정보 체크
    if (goal.getMentor() == null) {
      return Optional.empty(); // 멘토 정보 없음 - 스킵
    }

    MentorAdviceData data = dataCollector.collectData(user, goal);
    MentorAdvice advice = mentorAdviceService.generateAdvice(user, goal, data);

    return Optional.of(advice);
  }

  private Goal getCurrentProgressGoal(User user) {
    return getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream()
        .findFirst()
        .map(dto -> dto.getGoal())
        .orElseThrow(() -> new NotFoundException("진행중인 목표가 없습니다."));
  }

  private Optional<Goal> getCurrentProgressGoalOptional(User user) {
    return getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream()
        .findFirst()
        .map(dto -> dto.getGoal());
  }

  /** Goal의 mentor 정보가 조언 생성에 유효한지 검증합니다. */
  private void validateMentorForAdvice(Goal goal) {
    // Mentor is no longer part of Goal domain, no validation needed
  }
}
