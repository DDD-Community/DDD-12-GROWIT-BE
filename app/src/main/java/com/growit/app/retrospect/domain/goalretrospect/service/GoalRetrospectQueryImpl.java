package com.growit.app.retrospect.domain.goalretrospect.service;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.common.util.message.ErrorCode;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalRetrospectQueryImpl implements GoalRetrospectQuery {
  private final GoalRetrospectRepository goalRetrospectRepository;
  private final GoalRepository goalRepository;

  @Override
  public GoalRetrospect getMyGoalRetrospect(String id, String userId) throws NotFoundException {
    GoalRetrospect goalRetrospect =
        goalRetrospectRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorCode.RETROSPECT_NOT_FOUND.getCode()));

    // goalId를 통해 사용자 권한 확인
    Goal goal =
        goalRepository
            .findByIdAndUserId(goalRetrospect.getGoalId(), userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.RETROSPECT_NOT_FOUND.getCode()));

    return goalRetrospect;
  }
}
