package com.growit.app.fake.goal;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FakeGoalRepository implements GoalRepository {
  private final Map<String, List<Goal>> store = new ConcurrentHashMap<>();

  @Override
  public List<Goal> findAllByUserIdAndDeletedAtIsNull(String userId) {
    return store.getOrDefault(userId, Collections.emptyList()).stream()
        .filter(goal -> !goal.getDeleted())
        .collect(Collectors.toList());
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

  public void clear() {
    store.clear();
  }
}
