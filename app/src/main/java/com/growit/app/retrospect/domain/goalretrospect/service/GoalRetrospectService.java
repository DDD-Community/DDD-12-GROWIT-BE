package com.growit.app.retrospect.domain.goalretrospect.service;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.common.util.message.ErrorCode;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.todo.domain.ToDo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalRetrospectService implements GoalRetrospectQuery, AIAnalysis {
  private final GoalRetrospectRepository goalRetrospectRepository;

  @Override
  public GoalRetrospect getMyGoalRetrospect(String id, String userId) throws NotFoundException {
    return goalRetrospectRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.RETROSPECT_NOT_FOUND.getCode()));
  }

  @Override
  public Analysis generate(Goal goal, List<ToDo> todos) {
    return new Analysis("summary", "advice");
  }
}
