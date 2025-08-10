package com.growit.app.retrospect.usecase;

import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.domain.goalretrospect.dto.UpdateGoalRetrospectCommand;
import com.growit.app.retrospect.domain.goalretrospect.service.GoalRetrospectQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateGoalRetrospectUseCase {
  private final GoalRetrospectQuery goalRetrospectQuery;
  private final GoalRetrospectRepository goalRetrospectRepository;

  @Transactional
  public void execute(UpdateGoalRetrospectCommand command) {
    GoalRetrospect goalRetrospect = goalRetrospectQuery.getMyGoalRetrospect(command.id(), command.userId());
    goalRetrospect.updateContent(command.content());
    goalRetrospectRepository.save(goalRetrospect);
  }
}