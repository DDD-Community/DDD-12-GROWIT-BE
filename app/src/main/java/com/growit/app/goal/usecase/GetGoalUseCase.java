package com.growit.app.goal.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.controller.dto.GoalResponse;
import com.growit.app.goal.controller.mapper.GoalResponseMapper;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.user.domain.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetGoalUseCase {
  private final GoalRepository goalRepository;
  private final GoalResponseMapper goalResponseMapper;

  @Transactional(readOnly = true)
  public List<GoalResponse> getMyGoals(User user) {
    List<Goal> goals = goalRepository.findByUserId(user.getId());
    if (goals.isEmpty()) throw new NotFoundException("목표를 찾을 수 없습니다.");
    return goals.stream().map(goalResponseMapper::toResponse).collect(Collectors.toList());
  }
}
