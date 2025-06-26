package com.growit.app.goal.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.DeleteGoalCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteGoalUseCase {
  private final GoalRepository goalRepository;

  @Transactional
  public void execute(DeleteGoalCommand command) {
    String id = command.id();
    String userId = command.userId();

    Goal goal =
        goalRepository
            .findByUIdAndUserId(id, userId)
            .orElseThrow(() -> new NotFoundException("해당 목표가 없습니다."));

    // 3. 삭제
    goalRepository.deleteGoal(goal);
  }
}
