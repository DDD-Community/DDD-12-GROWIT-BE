package com.growit.app.retrospect.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.dto.GetRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectWithPlan;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetRetrospectUseCaseTest {

  @Mock private RetrospectQuery retrospectQuery;

  @Mock private GoalQuery goalQuery;

  @InjectMocks private GetRetrospectUseCase useCase;

  @Test
  void givenValidRetrospectIdAndUserId_whenExecute_thenReturnRetrospectWithPlan() {
    // given
    Retrospect retrospect = RetrospectFixture.defaultRetrospect();
    Goal goal = GoalFixture.defaultGoal();

    GetRetrospectCommand command =
        new GetRetrospectCommand(retrospect.getId(), retrospect.getUserId());

    when(retrospectQuery.getMyRetrospect(retrospect.getId(), retrospect.getUserId()))
        .thenReturn(retrospect);
    when(goalQuery.getMyGoal(retrospect.getGoalId(), retrospect.getUserId())).thenReturn(goal);

    // when
    RetrospectWithPlan result = useCase.execute(command);

    // then
    assertThat(result.getRetrospect()).isEqualTo(retrospect);
  }
}
