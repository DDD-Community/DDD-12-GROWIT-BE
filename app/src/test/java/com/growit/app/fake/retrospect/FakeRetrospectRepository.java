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
  public Optional<Retrospect> findByPlanId(String planId) {
    return store.values().stream().filter(r -> r.getPlanId().equals(planId)).findFirst();
  }
}
