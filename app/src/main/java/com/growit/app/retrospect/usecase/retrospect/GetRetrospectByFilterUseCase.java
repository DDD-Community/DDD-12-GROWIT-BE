package com.growit.app.retrospect.usecase.retrospect;

import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectQueryFilter;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import com.growit.app.retrospect.usecase.retrospect.dto.RetrospectWithPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetRetrospectByFilterUseCase {
  private final RetrospectQuery retrospectQuery;
  private final GoalQuery goalQuery;

  @Transactional(readOnly = true)
  public RetrospectWithPlan execute(RetrospectQueryFilter filter) {
    final Retrospect retrospect = retrospectQuery.getRetrospectByFilter(filter);
    // Plan functionality removed - return retrospect without plan
    return new RetrospectWithPlan(retrospect);
  }
}
