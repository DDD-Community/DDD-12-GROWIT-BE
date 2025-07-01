package com.growit.app.fake.retrospect;

import com.growit.app.goal.domain.retrospect.Retrospect;
import com.growit.app.goal.domain.retrospect.RetrospectRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FakeRetrospectRepository implements RetrospectRepository {
  private final Map<String, Retrospect> store = new ConcurrentHashMap<>();

  @Override
  public void saveRetrospect(Retrospect retrospect) {
    store.put(retrospect.getId(), retrospect);
  }

  @Override
  public Optional<Retrospect> findByGoalIdAndPlanId(String goalId, String planId) {
    return store.values().stream()
        .filter(
            retrospect ->
                retrospect.getGoalId().equals(goalId) && retrospect.getPlanId().equals(planId))
        .findFirst();
  }

  public void clear() {
    store.clear();
  }
}
