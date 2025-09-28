package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.user.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetGoalsByYearUseCase {
  private final GoalQuery goalQuery;

  @Transactional(readOnly = true)
  public List<Goal> getGoalsByYear(User user, int year) {
    return goalQuery.getGoalsByYear(user.getId(), year);
  }
}
