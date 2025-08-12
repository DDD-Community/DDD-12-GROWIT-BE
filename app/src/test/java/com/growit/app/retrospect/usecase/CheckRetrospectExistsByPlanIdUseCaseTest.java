package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.retrospect.domain.retrospect.dto.RetrospectQueryFilter;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import com.growit.app.retrospect.usecase.retrospect.CheckRetrospectExistsByPlanIdUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckRetrospectExistsByPlanIdUseCaseTest {

  @Mock private GoalValidator goalValidator;

  @Mock private RetrospectQuery retrospectQuery;

  @InjectMocks private CheckRetrospectExistsByPlanIdUseCase useCase;

  @Test
  void givenValidGoalAndPlan_whenRetrospectDoesNotExist_thenReturnFalse() {
    // given
    RetrospectQueryFilter filter = RetrospectFixture.defaultQueryFilter();

    // when
    doNothing()
        .when(goalValidator)
        .checkPlanExists(filter.userId(), filter.goalId(), filter.planId());
    when(retrospectQuery.getRetrospectByFilter(filter))
        .thenReturn(RetrospectFixture.defaultRetrospect());

    // then
    boolean result = useCase.execute(filter);
    assertFalse(result);
  }

  @Test
  void givenExistingRetrospect_whenCheck_thenReturnTrue() {
    // given
    RetrospectQueryFilter filter = RetrospectFixture.defaultQueryFilter();

    // when
    doNothing()
        .when(goalValidator)
        .checkPlanExists(filter.userId(), filter.goalId(), filter.planId());
    doThrow(new NotFoundException("Retrospect not exists"))
        .when(retrospectQuery)
        .getRetrospectByFilter(filter);

    // then
    boolean result = useCase.execute(filter);
    assertTrue(result);
  }
}
