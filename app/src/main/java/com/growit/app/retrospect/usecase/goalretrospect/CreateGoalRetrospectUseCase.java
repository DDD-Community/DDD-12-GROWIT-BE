package com.growit.app.retrospect.usecase.goalretrospect;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.util.message.ErrorCode;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.domain.goalretrospect.dto.CreateGoalRetrospectCommand;
import com.growit.app.retrospect.domain.goalretrospect.service.AIAnalysis;
import com.growit.app.retrospect.domain.goalretrospect.service.GoalRetrospectQuery;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import com.growit.app.retrospect.infrastructure.engine.dto.AnalysisDto;
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
  private final GoalRetrospectQuery goalRetrospectQuery;
  private final RetrospectQuery retrospectQuery;

  @Transactional
  public String execute(CreateGoalRetrospectCommand command) {
    if (goalRetrospectQuery.isExistsByGoalId(command.goalId())) {
      throw new BadRequestException("이미 목표 회고가 존재합니다"); // message unicode 변경으로 인한 메세지 지정
    }
    final Goal goal = goalQuery.getMyGoal(command.goalId(), command.userId());
    if (!goal.finished()) {
      throw new BadRequestException(ErrorCode.GOAL_RETROSPECT_GOAL_NOT_COMPLETED.getCode());
    }
    final List<ToDo> todos = toDoQuery.getToDosByGoalId(command.goalId());
    final int todoCompletedRate = ToDoUtils.calculateToDoCompletedRate(todos);
    final List<Retrospect> retrospects =
        retrospectQuery.getRetrospectsByGoalId(command.goalId(), command.userId());

    final Analysis analysis = aiAnalysis.generate(new AnalysisDto(goal, retrospects, todos));

    final GoalRetrospect goalRetrospect =
        GoalRetrospect.create(goal.getId(), todoCompletedRate, analysis, "");
    goalRetrospectRepository.save(goalRetrospect);

    return goalRetrospect.getId();
  }
}
