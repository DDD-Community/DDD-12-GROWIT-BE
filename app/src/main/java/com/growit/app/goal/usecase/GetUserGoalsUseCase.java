package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
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
  public List<Goal> getMyGoals(User user, GoalStatus status) {
    log.info("Cache Miss!");
    List<Goal> goals =
        goalRepository.findAllByUserId(user.getId()).stream()
            .filter(goal -> goal.checkProgress(status))
            .toList();
    if (goals.isEmpty()) return Collections.emptyList();
    return goals;
  }
}
