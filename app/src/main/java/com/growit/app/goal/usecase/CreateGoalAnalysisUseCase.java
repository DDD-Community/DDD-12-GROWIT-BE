package com.growit.app.goal.usecase;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.goal.domain.anlaysis.AnalysisRepository;
import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.goalretrospect.service.AIAnalysis;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.retrospect.infrastructure.engine.dto.AnalysisDto;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.service.ToDoQuery;
import com.growit.app.todo.domain.util.ToDoUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateGoalAnalysisUseCase {
  private final GoalQuery goalQuery;
  private final ToDoQuery toDoQuery;
  private final AnalysisRepository analysisRepository;
  private final AIAnalysis aiAnalysis;

  @Transactional
  public void execute(String goalId, String userId) {
    final Goal goal = goalQuery.getMyGoal(goalId, userId);
    if (!goal.isCompleted()) {
      throw new BadRequestException("목표가 완료되지 않았습니다");
    }

    final List<ToDo> todos = toDoQuery.getToDosByGoalId(goalId);
    final int todoCompletedRate = ToDoUtils.calculateToDoCompletedRate(todos);

    final Analysis analysis = aiAnalysis.generate(new AnalysisDto(goal, List.of(), todos));

    final GoalAnalysis goalAnalysis = GoalAnalysis.of(todoCompletedRate, analysis.summary());
    analysisRepository.save(goalId, goalAnalysis);
  }
}
