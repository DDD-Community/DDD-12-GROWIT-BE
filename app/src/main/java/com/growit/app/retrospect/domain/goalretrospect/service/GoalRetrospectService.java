package com.growit.app.retrospect.domain.goalretrospect.service;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.common.util.message.ErrorCode;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalRetrospectService implements GoalRetrospectQuery {
  private final GoalRetrospectRepository goalRetrospectRepository;

  @Override
  public GoalRetrospect getMyGoalRetrospect(String id, String userId) throws NotFoundException {
    return goalRetrospectRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.RETROSPECT_NOT_FOUND.getCode()));
  }

  @Override
  public boolean isExistsByGoalId(String goalId) {
    try {
      goalRetrospectRepository
          .findByGoalId(goalId)
          .orElseThrow(() -> new NotFoundException(ErrorCode.RETROSPECT_NOT_FOUND.getCode()));
      return true;
    } catch (NotFoundException e) {
      return false;
    }
  }
}
