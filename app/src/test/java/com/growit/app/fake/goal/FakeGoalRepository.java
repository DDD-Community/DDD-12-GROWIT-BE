package com.growit.app.fake.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FakeGoalRepository implements GoalRepository {
  private final Map<String, List<Goal>> store = new ConcurrentHashMap<>();

  @Override
  public List<Goal> findAllByUserIdAndDeletedAtIsNull(String userId) {
    return store.getOrDefault(userId, Collections.emptyList()).stream()
        .filter(goal -> !goal.getDeleted())
        .toList();
  }

  @Override
  public void saveGoal(Goal goal) {
    store.compute(
        goal.getUserId(),
        (userId, goals) -> {
          if (goals == null) {
            goals = new ArrayList<>();
          }
          goals.removeIf(g -> g.getId().equals(goal.getId()));
          goals.add(goal);
          return goals;
        });
  }

  @Override
  public Optional<Goal> findById(String goalId) {
    return store.values().stream()
        .flatMap(List::stream)
        .filter(goal -> goal.getId().equals(goalId))
        .findFirst();
  }

  @Override
  public Optional<Goal> findByIdAndUserId(String goalId, String userId) {
    return Optional.empty();
  }

  public void clear() {
    store.clear();
  }
}
