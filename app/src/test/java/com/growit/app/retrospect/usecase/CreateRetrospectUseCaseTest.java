package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;

import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectValidator;
import com.growit.app.retrospect.usecase.retrospect.CreateRetrospectUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateRetrospectUseCaseTest {

  @Mock private GoalValidator goalValidator;
  @Mock private RetrospectValidator retrospectValidator;

  @Mock private RetrospectRepository retrospectRepository;

  @InjectMocks private CreateRetrospectUseCase createRetrospectUseCase;

  @Test
  void givenValidCommand_whenExecute_thenReturnsRetrospectId() {
    // Given
    CreateRetrospectCommand command = RetrospectFixture.defaultCreateRetrospectCommand();

    doNothing()
        .when(goalValidator)
        .checkPlanExists(command.userId(), command.goalId(), command.planId());
    doNothing().when(retrospectValidator).checkUniqueRetrospect(command.planId());

    // When
    String result = createRetrospectUseCase.execute(command);

    // Then
    assertNotNull(result);
  }
}
