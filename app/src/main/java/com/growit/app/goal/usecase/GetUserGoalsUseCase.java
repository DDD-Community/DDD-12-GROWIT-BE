package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.anlaysis.AnalysisRepository;
import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.goal.usecase.dto.GoalWithAnalysisDto;
import com.growit.app.user.domain.user.User;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetUserGoalsUseCase {
  private final GoalQuery goalQuery;
  private final AnalysisRepository analysisRepository;

  @Transactional(readOnly = true)
  public List<GoalWithAnalysisDto> getMyGoals(User user, GoalStatus status) {
    List<Goal> goals =
        goalQuery.getAllGoalsByUserId(user.getId()).stream()
            .filter(goal -> filterByStatus(goal, status))
            .toList();

    if (goals.isEmpty()) return Collections.emptyList();

    return goals.stream()
        .map(
            goal -> {
              GoalAnalysis analysis = analysisRepository.findByGoalId(goal.getId()).orElse(null);
              return new GoalWithAnalysisDto(goal, analysis);
            })
        .toList();
  }

  private boolean filterByStatus(Goal goal, GoalStatus status) {
    if (status == GoalStatus.PROGRESS) {
      return goal.isInProgress();
    } else {
      return goal.isCompleted();
    }
  }
}
