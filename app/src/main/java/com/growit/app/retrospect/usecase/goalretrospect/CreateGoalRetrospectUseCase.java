package com.growit.app.retrospect.usecase.goalretrospect;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.util.message.ErrorCode;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.domain.goalretrospect.dto.CreateGoalRetrospectCommand;
import com.growit.app.retrospect.domain.goalretrospect.service.AIAnalysis;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.service.ToDoQuery;
import com.growit.app.todo.domain.util.ToDoUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateGoalRetrospectUseCase {
  private final GoalQuery goalQuery;
  private final ToDoQuery toDoQuery;
  private final AIAnalysis aiAnalysis;
  private final GoalRetrospectRepository goalRetrospectRepository;

  @Transactional
  public String execute(CreateGoalRetrospectCommand command) {
    final Goal goal = goalQuery.getMyGoal(command.goalId(), command.userId());
    if (!goal.finished()) {
      throw new BadRequestException(ErrorCode.GOAL_RETROSPECT_GOAL_NOT_COMPLETED.getCode());
    }
    final List<ToDo> todos = toDoQuery.getToDosByGoalId(command.goalId());
    final int todoCompletedRate = ToDoUtils.calculateToDoCompletedRate(todos);
    final Analysis analysis = aiAnalysis.generate(goal, todos);

    final GoalRetrospect goalRetrospect =
        GoalRetrospect.create(goal.getId(), todoCompletedRate, analysis, "");
    goalRetrospectRepository.save(goalRetrospect);

    return goalRetrospect.getId();
  }
}
