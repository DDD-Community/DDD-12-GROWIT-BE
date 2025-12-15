package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import com.growit.app.goal.domain.anlaysis.AnalysisRepository;
import com.growit.app.goal.usecase.dto.GoalWithAnalysisDto;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetGoalUseCase {
  private final GoalQuery goalQuery;
  private final AnalysisRepository analysisRepository;

  @Transactional(readOnly = true)
  public GoalWithAnalysisDto getGoal(String id, User user) {
    Goal goal = goalQuery.getMyGoal(id, user.getId());
    GoalAnalysis analysis = analysisRepository.findByGoalId(goal.getId())
        .orElse(null);

    return new GoalWithAnalysisDto(goal, analysis);
  }
}
