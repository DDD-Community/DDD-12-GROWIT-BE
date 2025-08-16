package com.growit.app.retrospect.usecase;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.goalretrospect.GoalRetrospectFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.usecase.goalretrospect.GetFinishedGoalsWithGoalRetrospectUseCase;
import com.growit.app.retrospect.usecase.goalretrospect.dto.GoalWithGoalRetrospectDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetFinishedGoalsWithGoalRetrospectUseCaseTest {
  @Mock private GoalQuery goalQuery;
  @Mock private GoalRetrospectRepository goalRetrospectRepository;

  @InjectMocks private GetFinishedGoalsWithGoalRetrospectUseCase useCase;

  @Test
  void givenGoalWithRetrospect_whenExecute_thenReturnDtoWithRetrospect() {
    // given
    String userId = "user1";
    int year = 2025;
    Goal goal = GoalFixture.defaultGoal();
    GoalRetrospect goalRetrospect = GoalRetrospectFixture.defaultGoalRetrospect();

    when(goalQuery.getFinishedGoalsByYear(userId, year)).thenReturn(List.of(goal));
    when(goalRetrospectRepository.findByGoalId(goal.getId()))
        .thenReturn(Optional.of(goalRetrospect));

    // when
    List<GoalWithGoalRetrospectDto> result = useCase.execute(userId, year);

    // then
    assertThat(result).hasSize(1);
  }
}
