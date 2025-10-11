package com.growit.app.advice.usecase;

import static com.growit.app.common.util.message.ErrorCode.GOAL_PROGRESS_NOTFOUND;

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
  private final GenerateMentorAdviceUseCase generateMentorAdviceUseCase;

  public MentorAdvice execute(User user) {
    Goal currentGoal = getCurrentProgressGoal(user);
    MentorAdvice existingAdvice = findExistingAdvice(user.getId(), currentGoal.getId());

    if (existingAdvice == null) {
      return createNewMentorAdvice(user);
    }

    if (shouldFetchNewAdvice(existingAdvice)) {
      return createNewMentorAdvice(user);
    }

    return updateExistingAdviceAsChecked(existingAdvice);
  }

  private Goal getCurrentProgressGoal(User user) {
    return getUserGoalsUseCase.getMyGoals(user, GoalStatus.PROGRESS).stream()
        .findFirst()
        .orElseThrow(() -> new NotFoundException(GOAL_PROGRESS_NOTFOUND.getCode()));
  }

  private MentorAdvice findExistingAdvice(String userId, String goalId) {
    return mentorAdviceRepository.findByUserIdAndGoalId(userId, goalId).orElse(null);
  }

  private boolean shouldFetchNewAdvice(MentorAdvice mentorAdvice) {
    return mentorAdvice.shouldFetch();
  }

  private MentorAdvice updateExistingAdviceAsChecked(MentorAdvice mentorAdvice) {
    if (!mentorAdvice.isChecked()) {
      mentorAdvice.updateIsChecked(true);
      mentorAdviceRepository.save(mentorAdvice);
    }
    return mentorAdvice;
  }

  private MentorAdvice createNewMentorAdvice(User user) {
    MentorAdvice newAdvice = generateMentorAdviceUseCase.execute(user);
    mentorAdviceRepository.save(newAdvice);
    return newAdvice;
  }
}
