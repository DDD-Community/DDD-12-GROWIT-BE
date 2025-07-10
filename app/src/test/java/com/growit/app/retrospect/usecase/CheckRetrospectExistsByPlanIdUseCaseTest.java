package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.retrospect.domain.retrospect.dto.CheckRetrospectExistsQueryFilter;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckRetrospectExistsByPlanIdUseCaseTest {

  @Mock private GoalValidator goalValidator;

  @Mock private RetrospectValidator retrospectValidator;

  @InjectMocks private CheckRetrospectExistsByPlanIdUseCase useCase;

  @Test
  void givenValidGoalAndPlan_whenRetrospectDoesNotExist_thenReturnFalse() {
    // given
    String userId = "user-1";
    String goalId = "goal-123";
    String planId = "plan-123";
    CheckRetrospectExistsQueryFilter filter =
        new CheckRetrospectExistsQueryFilter(userId, goalId, planId);

    // when
    doNothing().when(goalValidator).checkPlanExists(userId, goalId, planId);
    doNothing().when(retrospectValidator).checkUniqueRetrospect(planId);

    // then
    boolean result = useCase.execute(filter);
    assertFalse(result);
  }

  @Test
  void givenExistingRetrospect_whenCheck_thenReturnTrue() {
    // given
    String userId = "user-2";
    String goalId = "goal-456";
    String planId = "plan-456";
    CheckRetrospectExistsQueryFilter filter =
        new CheckRetrospectExistsQueryFilter(userId, goalId, planId);

    // when
    doNothing().when(goalValidator).checkPlanExists(userId, goalId, planId);
    doThrow(new BadRequestException("Retrospect exists"))
        .when(retrospectValidator)
        .checkUniqueRetrospect(planId);

    // then
    boolean result = useCase.execute(filter);
    assertTrue(result);
  }
}
