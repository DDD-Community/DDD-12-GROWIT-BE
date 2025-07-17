package com.growit.app.retrospect.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.goal.PlanFixture;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.dto.GetRetrospectByGoalIdAndPlanIdQueryFilter;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import com.growit.app.retrospect.usecase.dto.RetrospectWithPlan;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetRetrospectByGoalIdAndPlanIdUseCaseTest {

  @Mock private RetrospectQuery retrospectQuery;
  @Mock private GoalQuery goalQuery;
  @Mock private GoalValidator goalValidator;

  @InjectMocks private GetRetrospectByGoalIdAndPlanIdUseCase useCase;

  @Test
  void 성공적으로_goalId와_planId로_회고를_조회한다() {
    // given
    String userId = "user-123";
    String goalId = "goal-456";
    String planId = "plan-789";
    
    Retrospect retrospect = RetrospectFixture.customRetrospect(
        "retro-001", userId, goalId, planId, "회고 내용");
    Plan plan = PlanFixture.customPlan(planId, 1, "주간 목표", null, null);
    Goal goal = GoalFixture.customGoal(goalId, userId, null, null, null, List.of(plan));
    
    GetRetrospectByGoalIdAndPlanIdQueryFilter filter = 
        new GetRetrospectByGoalIdAndPlanIdQueryFilter(goalId, planId, userId);

    when(retrospectQuery.getMyRetrospectByGoalIdAndPlanId(goalId, planId, userId))
        .thenReturn(retrospect);
    when(goalQuery.getMyGoal(retrospect.getGoalId(), retrospect.getUserId()))
        .thenReturn(goal);

    // when
    RetrospectWithPlan result = useCase.execute(filter);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getRetrospect()).isEqualTo(retrospect);
    assertThat(result.getRetrospect().getGoalId()).isEqualTo(goalId);
    assertThat(result.getRetrospect().getPlanId()).isEqualTo(planId);
    assertThat(result.getRetrospect().getUserId()).isEqualTo(userId);
    assertThat(result.getRetrospect().getContent()).isEqualTo("회고 내용");
    assertThat(result.getPlan()).isNotNull();
    
    verify(goalValidator).checkPlanExists(userId, goalId, planId);
    verify(retrospectQuery).getMyRetrospectByGoalIdAndPlanId(goalId, planId, userId);
    verify(goalQuery).getMyGoal(retrospect.getGoalId(), retrospect.getUserId());
  }

  @Test
  void 회고가_존재하지_않으면_NotFoundException을_던진다() {
    // given
    String userId = "user-123";
    String goalId = "goal-456";
    String planId = "plan-789";
    
    GetRetrospectByGoalIdAndPlanIdQueryFilter filter = 
        new GetRetrospectByGoalIdAndPlanIdQueryFilter(goalId, planId, userId);

    when(retrospectQuery.getMyRetrospectByGoalIdAndPlanId(goalId, planId, userId))
        .thenThrow(new NotFoundException("RETROSPECT_NOT_FOUND"));

    // when & then
    assertThatThrownBy(() -> useCase.execute(filter))
        .isInstanceOf(NotFoundException.class);
        
    verify(goalValidator).checkPlanExists(userId, goalId, planId);
    verify(retrospectQuery).getMyRetrospectByGoalIdAndPlanId(goalId, planId, userId);
  }
}