package com.growit.app.retrospect.usecase;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.retrospect.domain.retrospect.dto.CheckRetrospectExistsQueryFilter;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckRetrospectExistsByPlanIdUseCase {
  private final GoalValidator goalValidator;
  private final RetrospectValidator validator;

  @Transactional(readOnly = true)
  public boolean execute(CheckRetrospectExistsQueryFilter filter) {
    goalValidator.checkPlanExists(filter.userId(), filter.goalId(), filter.planId());
    return isRetrospectExists(filter);
  }

  private boolean isRetrospectExists(CheckRetrospectExistsQueryFilter filter) {
    try {
      validator.checkUniqueRetrospect(filter.planId());
      return false;
    } catch (BadRequestException e) {
      return true;
    }
  }
}
