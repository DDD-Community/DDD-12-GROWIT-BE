package com.growit.app.fake.retrospect;

import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FakeRetrospectRepository implements RetrospectRepository {
  private final Map<String, Retrospect> store = new ConcurrentHashMap<>();

  @Override
  public void saveRetrospect(Retrospect retrospect) {
    store.put(retrospect.getId(), retrospect);
  }

  @Override
  public Optional<Retrospect> findById(String id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public Optional<Retrospect> findByIdAndUserId(String id, String userId) {
    Retrospect retrospect = store.get(id);
    if (retrospect == null || !retrospect.getUserId().equals(userId)) {
      return Optional.empty();
    }
    return Optional.of(retrospect);
  }

  @Override
  public Optional<Retrospect> findByPlanId(String planId) {
    return store.values().stream().filter(r -> r.getPlanId().equals(planId)).findFirst();
  }

  @Override
  public Optional<Retrospect> findByGoalIdAndPlanIdAndUserId(String goalId, String planId, String userId) {
    return store.values().stream()
        .filter(r -> r.getGoalId().equals(goalId) && 
                     r.getPlanId().equals(planId) && 
                     r.getUserId().equals(userId))
        .findFirst();
  }
}
