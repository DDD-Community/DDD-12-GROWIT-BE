package com.growit.app.advice.usecase;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.MentorAdviceRepository;
import com.growit.app.advice.domain.mentor.service.MentorService;
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

  private static final String NO_PROGRESS_GOAL_MESSAGE = "현재 진행중인 목표가 존재하지 않습니다.";

  private final MentorAdviceRepository mentorAdviceRepository;
  private final GetUserGoalsUseCase getUserGoalsUseCase;
  private final MentorService mentorService;

  public MentorAdvice execute(User user) {
    Goal currentGoal = getCurrentProgressGoal(user);
    return getOrCreateMentorAdvice(user.getId(), currentGoal.getId());
  }

  private Goal getCurrentProgressGoal(User user) {
    return getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream()
        .findFirst()
        .orElseThrow(() -> new NotFoundException(NO_PROGRESS_GOAL_MESSAGE));
  }

  private MentorAdvice getOrCreateMentorAdvice(String userId, String goalId) {
    return mentorAdviceRepository
        .findByUserIdAndGoalId(userId, goalId)
        .map(this::markAsCheckedIfNeeded)
        .orElseGet(() -> createNewMentorAdvice(userId, goalId));
  }

  private MentorAdvice markAsCheckedIfNeeded(MentorAdvice mentorAdvice) {
    if (!mentorAdvice.isChecked()) {
      mentorAdvice.updateIsChecked(true);
      mentorAdviceRepository.save(mentorAdvice);
    }
    return mentorAdvice;
  }

  private MentorAdvice createNewMentorAdvice(String userId, String goalId) {
    MentorAdvice newAdvice = mentorService.getMentorAdvice(userId, goalId);
    mentorAdviceRepository.save(newAdvice);
    return newAdvice;
  }
}
