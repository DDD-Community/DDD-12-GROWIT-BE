package com.growit.app.goal.usecase;

import com.growit.app.goal.controller.dto.GoalResponse;
import com.growit.app.goal.controller.mapper.GoalResponseMapper;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.service.GoalNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetGoalUseCase {
  private final GoalRepository goalRepository;
  private final GoalResponseMapper goalResponseMapper;

  @Transactional(readOnly = true)
  public GoalResponse getGoal(String uid) {
    Goal goal =
        goalRepository.findByIdWithAllRelations(uid).orElseThrow(GoalNotFoundException::new);
    return goalResponseMapper.toResponse(goal);
  }
}
