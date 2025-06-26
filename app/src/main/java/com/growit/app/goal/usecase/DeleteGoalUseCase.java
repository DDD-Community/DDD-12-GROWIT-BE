package com.growit.app.goal.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.DeleteGoalCommand;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteGoalUseCase {
  private final GoalRepository goalRepository;
  private final GoalValidator goalValidator;

  @Transactional
  public void execute(DeleteGoalCommand command) {
    Goal goal =
        goalRepository
            .findById(command.id())
            .orElseThrow(() -> new NotFoundException("해당 되는 목표가 없습니다."));

    goalValidator.checkMyGoal(goal, command.userId());

    goal.markAsDeleted();
    goalRepository.saveGoal(goal);
  }
}
