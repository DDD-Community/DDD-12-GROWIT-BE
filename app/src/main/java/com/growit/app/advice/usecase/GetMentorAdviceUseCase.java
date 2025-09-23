package com.growit.app.advice.usecase;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.MentorAdviceRepository;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GetMentorAdviceUseCase {

  private final MentorAdviceRepository mentorAdviceRepository;
  private final GetUserGoalsUseCase getUserGoalsUseCase;

  public MentorAdvice execute(User user) {
    // 1. 현재 실행중인 목표가 있는지 확인
    final Goal goal =
        getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException("현재 진행중인 목표가 존재하지 않습니다."));
    // 2. 기존 조언이 있는지 확인
    return mentorAdviceRepository
        .findByUserIdAndGoalId(user.getId(), goal.getId())
        .map(this::handleExistingAdvice)
        .orElse(null);
  }

  private MentorAdvice handleExistingAdvice(MentorAdvice existingAdvice) {
    // isChecked 가 false 면 true 로 변경하고 저장
    if (!existingAdvice.isChecked()) {
      existingAdvice.updateIsChecked(true);
      mentorAdviceRepository.save(existingAdvice);
    }

    return existingAdvice;
  }
}
