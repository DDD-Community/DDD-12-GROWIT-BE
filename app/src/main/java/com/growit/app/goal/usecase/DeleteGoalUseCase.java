package com.growit.app.goal.usecase;

import com.growit.app.common.exception.BadRequestException;
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
    Goal goal =
        goalRepository
            .findByUid(command.uid())
            .orElseThrow(() -> new NotFoundException("해당 되는 목표가 없습니다."));

    if (!goal.getUserId().equals(command.userId())) {
      throw new BadRequestException("삭제 권한이 없습니다.");
    }
    goal.markAsDeleted();
    goalRepository.saveGoal(goal);
  }
}
