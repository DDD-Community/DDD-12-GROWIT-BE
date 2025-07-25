package com.growit.app.fake.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FakeGoalRepository implements GoalRepository {
  private final Map<String, Goal> store = new ConcurrentHashMap<>();

  @Override
  public List<Goal> findAllByUserId(String userId) {
    return store.values().stream()
        .filter(goal -> goal.getUserId().equals(userId) && !goal.getDeleted())
        .toList();
  }

  @Override
  public void saveGoal(Goal goal) {
    store.put(goal.getId(), goal);
  }

  @Override
  public Optional<Goal> findById(String goalId) {
    return Optional.ofNullable(store.get(goalId));
  }

  @Override
  public Optional<Goal> findByIdAndUserId(String id, String userId) {
    Goal goal = store.get(id);
    if (goal == null || !goal.getUserId().equals(userId) || goal.getDeleted()) {
      return Optional.empty();
    }
    return Optional.of(goal);
  }

  public void clear() {
    store.clear();
  }
}
