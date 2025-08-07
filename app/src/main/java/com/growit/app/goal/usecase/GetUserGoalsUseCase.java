package com.growit.app.goal.usecase;

import static com.growit.app.common.util.message.ErrorCode.GOAL_PROGRESS_NOTFOUND;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.user.domain.user.User;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetUserGoalsUseCase {
  private final GoalRepository goalRepository;

  @Cacheable(cacheNames = "goalCache", value = "goalCache", key = "#user.id")
  @Transactional(readOnly = true)
  public List<Goal> getMyGoals(User user) {
    log.info("Cache Miss!");

    List<Goal> goals = goalRepository.findAllByUserId(user.getId());
    if (goals.isEmpty()) return Collections.emptyList();

    return goals;
  }

  public List<Goal> getFinishMyGoal(User user) {
    List<Goal> goals = goalRepository.findByUserIdAndEndDate(user.getId());
    if (goals.isEmpty()) return Collections.emptyList();

    return goals;
  }

  public Goal getProgressMyGoal(User user) {
    return goalRepository
        .findByUserIdAndStartDateAndEndDate(user.getId())
        .orElseThrow(() -> new NotFoundException(GOAL_PROGRESS_NOTFOUND.getCode()));
  }
}
