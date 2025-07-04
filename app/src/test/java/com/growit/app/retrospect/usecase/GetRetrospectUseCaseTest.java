package com.growit.app.retrospect.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.goal.PlanFixture;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.domain.retrospect.command.GetRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectWithPlan;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class GetRetrospectUseCaseTest {

  @Test
  void execute_success_withFixture() {
    Retrospect retrospect = RetrospectFixture.defaultRetrospect();
    Plan plan = PlanFixture.customPlan(retrospect.getPlanId(), null, null, null, null);
    Goal goal =
        GoalFixture.customGoal(retrospect.getGoalId(), null, null, null, null, List.of(plan));

    GetRetrospectCommand command =
        new GetRetrospectCommand(retrospect.getId(), retrospect.getUserId());

    RetrospectRepository retrospectRepository = mock(RetrospectRepository.class);
    GoalRepository goalRepository = mock(GoalRepository.class);

    given(retrospectRepository.findById(retrospect.getId())).willReturn(Optional.of(retrospect));
    given(goalRepository.findById(retrospect.getGoalId())).willReturn(Optional.of(goal));

    GetRetrospectUseCase useCase = new GetRetrospectUseCase(retrospectRepository, goalRepository);

    RetrospectWithPlan result = useCase.execute(command);
    System.out.println(result.getRetrospect() == retrospect);
    System.out.println(result.getPlan() == plan);

    // Assert
    assertThat(result.getRetrospect()).isEqualTo(retrospect);
    assertThat(result.getPlan()).isEqualTo(plan);
  }

  @Test
  void execute_retrospect_not_found() {

    Retrospect retrospect = RetrospectFixture.defaultRetrospect();
    GetRetrospectCommand command =
        new GetRetrospectCommand(retrospect.getId(), retrospect.getUserId());

    RetrospectRepository retrospectRepository = mock(RetrospectRepository.class);
    GoalRepository goalRepository = mock(GoalRepository.class);

    given(retrospectRepository.findById(retrospect.getId())).willReturn(Optional.empty());

    GetRetrospectUseCase useCase = new GetRetrospectUseCase(retrospectRepository, goalRepository);

    // Act & Assert
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("회고를 찾을 수 없습니다.");
  }

  @Test
  void execute_goal_not_found() {
    // Arrange
    Retrospect retrospect = RetrospectFixture.defaultRetrospect();
    GetRetrospectCommand command =
        new GetRetrospectCommand(retrospect.getId(), retrospect.getUserId());

    RetrospectRepository retrospectRepository = mock(RetrospectRepository.class);
    GoalRepository goalRepository = mock(GoalRepository.class);

    given(retrospectRepository.findById(retrospect.getId())).willReturn(Optional.of(retrospect));
    given(goalRepository.findById(retrospect.getGoalId())).willReturn(Optional.empty());

    GetRetrospectUseCase useCase = new GetRetrospectUseCase(retrospectRepository, goalRepository);

    // Act & Assert
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("목표가 존재하지 않습니다.");
  }

  @Test
  void execute_plan_not_found() {
    // Arrange
    Retrospect retrospect = RetrospectFixture.defaultRetrospect();
    Goal goal = GoalFixture.defaultGoal();

    GetRetrospectCommand command =
        new GetRetrospectCommand(retrospect.getId(), retrospect.getUserId());

    RetrospectRepository retrospectRepository = mock(RetrospectRepository.class);
    GoalRepository goalRepository = mock(GoalRepository.class);

    given(retrospectRepository.findById(retrospect.getId())).willReturn(Optional.of(retrospect));
    given(goalRepository.findById(retrospect.getGoalId())).willReturn(Optional.of(goal));

    GetRetrospectUseCase useCase = new GetRetrospectUseCase(retrospectRepository, goalRepository);

    // Act & Assert
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(NotFoundException.class)
        .hasMessageContaining("일치하는 Plan이 없습니다.");
  }
}
