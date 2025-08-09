package com.growit.app.fake.goalretrospect;

import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeGoalRetrospectRepository implements GoalRetrospectRepository {
  private final Map<String, GoalRetrospect> storage = new HashMap<>();

  @Override
  public void save(GoalRetrospect goalRetrospect) {
    storage.put(goalRetrospect.getId(), goalRetrospect);
  }

  @Override
  public Optional<GoalRetrospect> findById(String id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Optional<GoalRetrospect> findByGoalId(String goalId) {
    return storage.values().stream().filter(gr -> gr.getGoalId().equals(goalId)).findFirst();
  }

  @Override
  public List<GoalRetrospect> findAllByGoalId(String goalId) {
    return storage.values().stream().filter(gr -> gr.getGoalId().equals(goalId)).toList();
  }

  public void clear() {
    storage.clear();
  }
}
