package com.growit.app.fake.goal;

import com.growit.app.goal.domain.anlaysis.AnalysisRepository;
import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeAnalysisRepository implements AnalysisRepository {
  private final Map<String, GoalAnalysis> store = new HashMap<>();

  @Override
  public Optional<GoalAnalysis> findByGoalId(String goalId) {
    return Optional.ofNullable(store.get(goalId));
  }

  @Override
  public void save(String goalId, GoalAnalysis analysis) {
    store.put(goalId, analysis);
  }

  @Override
  public void deleteByGoalId(String goalId) {
    store.remove(goalId);
  }

  public void clear() {
    store.clear();
  }
}
