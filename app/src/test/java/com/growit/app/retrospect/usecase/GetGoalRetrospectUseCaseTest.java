package com.growit.app.retrospect.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.growit.app.fake.goalretrospect.GoalRetrospectFixture;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospect;
import com.growit.app.retrospect.domain.goalretrospect.service.GoalRetrospectQuery;
import com.growit.app.retrospect.usecase.goalretrospect.GetGoalRetrospectUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetGoalRetrospectUseCaseTest {

  @Mock private GoalRetrospectQuery goalRetrospectQuery;

  @InjectMocks private GetGoalRetrospectUseCase getGoalRetrospectUseCase;

  @Test
  void should_return_goal_retrospect_when_valid_parameters_provided() {
    // given
    String id = "goal-retrospect-id";
    String userId = "user-id";
    GoalRetrospect expectedGoalRetrospect = GoalRetrospectFixture.defaultGoalRetrospect();

    given(goalRetrospectQuery.getMyGoalRetrospect(id, userId)).willReturn(expectedGoalRetrospect);

    // when
    GoalRetrospect result = getGoalRetrospectUseCase.execute(id, userId);

    // then
    assertThat(result).isEqualTo(expectedGoalRetrospect);
  }
}
