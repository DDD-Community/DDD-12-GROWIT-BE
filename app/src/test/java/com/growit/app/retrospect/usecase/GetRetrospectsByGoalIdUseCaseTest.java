package com.growit.app.retrospect.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.goal.PlanFixture;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import com.growit.app.retrospect.domain.retrospect.vo.KPT;
import com.growit.app.retrospect.usecase.retrospect.GetRetrospectsByGoalIdUseCase;
import com.growit.app.retrospect.usecase.retrospect.dto.RetrospectWithPlan;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetRetrospectsByGoalIdUseCaseTest {

  @Mock private RetrospectQuery retrospectQuery;
  @Mock private GoalQuery goalQuery;

  @InjectMocks private GetRetrospectsByGoalIdUseCase useCase;

  @Test
  void givenValidGoalIdAndUserId_whenExecute_thenReturnRetrospectListWithPlans() {
    // given
    String goalId = "goalId";
    String userId = "userId";
    String planId1 = "plan-1";
    String planId2 = "plan-2";

    KPT kpt1 = new KPT("유지할 것 1", "문제점 1", "다음 시도 1");
    Retrospect retrospect1 =
        RetrospectFixture.customRetrospect("retrospect-1", userId, goalId, planId1, kpt1);
    KPT kpt2 = new KPT("유지할 것 2", "문제점 2", "다음 시도 2");
    Retrospect retrospect2 =
        RetrospectFixture.customRetrospect("retrospect-2", userId, goalId, planId2, kpt2);
    List<Retrospect> retrospects = List.of(retrospect1, retrospect2);

    Plan plan1 = PlanFixture.customPlan(planId1, 1, null, null, null);
    Plan plan2 = PlanFixture.customPlan(planId2, 2, null, null, null);
    Goal goal =
        GoalFixture.customGoal(goalId, userId, null, null, null, null, null, List.of(plan1, plan2));

    when(retrospectQuery.getRetrospectsByGoalId(goalId, userId)).thenReturn(retrospects);
    when(goalQuery.getMyGoal(goalId, userId)).thenReturn(goal);

    // when
    List<RetrospectWithPlan> results = useCase.execute(goalId, userId);

    // then
    assertThat(results).hasSize(2);
  }

  @Test
  void givenGoalIdWithNoRetrospects_whenExecute_thenReturnPlansWithNoRetrospects() {
    // given
    String goalId = "goalId";
    String userId = "userId";
    List<Retrospect> retrospects = List.of();
    Goal goal = GoalFixture.defaultGoal();

    when(retrospectQuery.getRetrospectsByGoalId(goalId, userId)).thenReturn(retrospects);
    when(goalQuery.getMyGoal(goalId, userId)).thenReturn(goal);

    // when
    List<RetrospectWithPlan> results = useCase.execute(goalId, userId);

    // then
    assertThat(results).hasSize(goal.getPlans().size());
  }
}
