package com.growit.app.retrospect.usecase.retrospect;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectQueryFilter;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckRetrospectExistsByPlanIdUseCase {
  private final GoalValidator goalValidator;
  private final RetrospectQuery retrospectQuery;

  @Transactional(readOnly = true)
  public boolean execute(RetrospectQueryFilter filter) {
    // 목표 존재 여부 확인은 실제 목표 조회 시 처리됨 (planId 제거됨)
    return isExists(filter);
  }

  private boolean isExists(RetrospectQueryFilter filter) {
    try {
      retrospectQuery.getRetrospectByFilter(filter);
      return false;
    } catch (NotFoundException e) {
      return true;
    }
  }
}
