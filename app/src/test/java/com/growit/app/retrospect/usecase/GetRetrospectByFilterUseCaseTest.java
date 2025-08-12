package com.growit.app.retrospect.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectQueryFilter;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import com.growit.app.retrospect.usecase.retrospect.GetRetrospectByFilterUseCase;
import com.growit.app.retrospect.usecase.retrospect.dto.RetrospectWithPlan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetRetrospectByFilterUseCaseTest {

  @Mock private RetrospectQuery retrospectQuery;
  @Mock private GoalQuery goalQuery;

  @InjectMocks private GetRetrospectByFilterUseCase useCase;

  @Test
  void givenValidQueryFilter_whenExecute_thenReturnRetrospectWithPlan() {
    // given
    Retrospect retrospect = RetrospectFixture.defaultRetrospect();
    Goal goal = GoalFixture.defaultGoal();
    RetrospectQueryFilter filter = RetrospectFixture.defaultQueryFilter();
    when(retrospectQuery.getRetrospectByFilter(filter)).thenReturn(retrospect);
    when(goalQuery.getMyGoal(retrospect.getGoalId(), retrospect.getUserId())).thenReturn(goal);

    // when
    RetrospectWithPlan result = useCase.execute(filter);

    // then
    assertThat(result.getRetrospect()).isEqualTo(retrospect);
  }
}
