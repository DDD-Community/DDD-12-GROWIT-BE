package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.user.domain.user.User;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetUserGoalsUseCase {
  private final GoalRepository goalRepository;

  @Transactional(readOnly = true)
  public List<Goal> getMyGoals(User user) {
    List<Goal> goals = goalRepository.findByUserId(user.getId());
    if (goals.isEmpty()) return Collections.emptyList();
    return goals;
  }
}
