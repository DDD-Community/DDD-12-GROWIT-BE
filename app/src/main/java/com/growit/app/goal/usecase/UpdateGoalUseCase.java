package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.goal.domain.goal.vo.GoalUpdateStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateGoalUseCase {
  private static final int BATCH_SIZE = 1000;
  private final GoalRepository goalRepository;
  private final GoalValidator goalValidator;
  private final GoalQuery goalQuery;

  @Transactional
  public void execute(UpdateGoalCommand command) {
    final Goal goal = goalQuery.getMyGoal(command.id(), command.userId());
    goalValidator.checkGoalDuration(command.duration());
    goalValidator.checkPlans(command.duration(), command.plans());
    goal.updateByCommand(command, goal.getUpdateStatus());

    goalRepository.saveGoal(goal);
  }

  @Transactional
  @Async
  public void updateEndedGoals(LocalDate today) {
    int page = 0;
    List<String> ids;
    do {
      ids = goalRepository.findEndedCandidateIds(today, page, BATCH_SIZE);
      List<Goal> goals = goalRepository.findAllByIds(ids);

      for (Goal g : goals) {
        g.updateByGoalUpdateStatus(GoalUpdateStatus.ENDED);
        goalRepository.saveGoal(g);
      }
      goalRepository.flushAndClear();
      page++;
    } while (!ids.isEmpty());
  }
}
