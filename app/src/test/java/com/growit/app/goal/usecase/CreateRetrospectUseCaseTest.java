package com.growit.app.goal.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.growit.app.goal.domain.goal.retrospect.RetrospectRepository;
import com.growit.app.goal.domain.goal.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.goal.domain.goal.retrospect.service.RetrospectValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateRetrospectUseCaseTest {

  @Mock private RetrospectValidator retrospectValidator;
  @Mock private RetrospectRepository retrospectRepository;

  private CreateRetrospectUseCase createRetrospectUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    createRetrospectUseCase = new CreateRetrospectUseCase(retrospectValidator, retrospectRepository);
  }

  @Test
  void givenValidCommand_whenExecute_thenReturnRetrospectId() {
    // Given
    CreateRetrospectCommand command = new CreateRetrospectCommand(
        "goal-123",
        "plan-456", 
        "user-789",
        "이번 주 계획을 잘 수행했습니다. 다음 주에는 더 많은 성과를 내도록 노력하겠습니다."
    );

    doNothing().when(retrospectValidator).validateContent(command.content());
    doNothing().when(retrospectValidator).validatePlanExists(command.goalId(), command.planId());
    doNothing().when(retrospectValidator).validateUniqueRetrospect(command.goalId(), command.planId());

    // When
    String retrospectId = createRetrospectUseCase.execute(command);

    // Then
    assertNotNull(retrospectId);
    
    // Verify all validations were called
    verify(retrospectValidator).validateContent(command.content());
    verify(retrospectValidator).validatePlanExists(command.goalId(), command.planId());
    verify(retrospectValidator).validateUniqueRetrospect(command.goalId(), command.planId());
    
    // Verify repository save was called
    verify(retrospectRepository).saveRetrospect(any());
  }
}