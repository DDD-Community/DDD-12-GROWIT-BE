package com.growit.app.retrospect.usecase;

import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.service.GoalRetrospectQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetGoalRetrospectUseCase {
  private final GoalRetrospectQuery goalRetrospectQuery;

  @Transactional(readOnly = true)
  public GoalRetrospect execute(String id, String userId) {
    return goalRetrospectQuery.getMyGoalRetrospect(id, userId);
  }
}
