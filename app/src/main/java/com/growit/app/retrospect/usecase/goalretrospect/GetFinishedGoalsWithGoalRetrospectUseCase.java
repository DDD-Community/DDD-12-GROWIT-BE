package com.growit.app.retrospect.usecase.goalretrospect;

import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.usecase.goalretrospect.dto.GoalWithGoalRetrospectDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetFinishedGoalsWithGoalRetrospectUseCase {
  private final GoalQuery goalQuery;
  private final GoalRetrospectRepository goalRetrospectRepository;

  @Transactional(readOnly = true)
  public List<GoalWithGoalRetrospectDto> execute(String userId, int year) {
    var goals = goalQuery.getFinishedGoalsByYear(userId, year);

    return goals.stream()
        .map(goal -> {
          var retrospectOpt = goalRetrospectRepository.findByGoalId(goal.getId());
          return new GoalWithGoalRetrospectDto(goal, retrospectOpt.orElse(null));
        })
        .toList();
  }
}
