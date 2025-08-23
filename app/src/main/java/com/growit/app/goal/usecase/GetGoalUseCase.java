package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetGoalUseCase {
  private final GoalQuery goalQuery;

  @Transactional(readOnly = true)
  public Goal getGoal(String id, User user) {
    return goalQuery.getMyGoal(id, user.getId());
  }
}
