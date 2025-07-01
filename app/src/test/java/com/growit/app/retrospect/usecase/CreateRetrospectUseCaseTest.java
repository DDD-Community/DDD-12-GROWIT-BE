package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateRetrospectUseCaseTest {

  @Mock private RetrospectValidator retrospectValidator;
  @Mock private RetrospectRepository retrospectRepository;

  @InjectMocks private CreateRetrospectUseCase createRetrospectUseCase;

  @Test
  void givenValidCommand_whenExecute_thenReturnsRetrospectId() {
    // Given
    CreateRetrospectCommand command = RetrospectFixture.defaultCreateRetrospectCommand();
    
    doNothing().when(retrospectValidator).validateContent(anyString());
    doNothing().when(retrospectValidator).validatePlanExists(anyString(), anyString());
    doNothing().when(retrospectValidator).validateUniqueRetrospect(anyString(), anyString());

    // When
    String retrospectId = createRetrospectUseCase.execute(command);

    // Then
    assertNotNull(retrospectId);
    
    // Verify all validations were called
    verify(retrospectValidator).validateContent(command.content());
    verify(retrospectValidator).validatePlanExists(command.goalId(), command.planId());
    verify(retrospectValidator).validateUniqueRetrospect(command.goalId(), command.planId());
    
    // Verify repository save was called with correct retrospect
    ArgumentCaptor<Retrospect> retrospectCaptor = ArgumentCaptor.forClass(Retrospect.class);
    verify(retrospectRepository).saveRetrospect(retrospectCaptor.capture());
    
    Retrospect savedRetrospect = retrospectCaptor.getValue();
    assertEquals(command.goalId(), savedRetrospect.getGoalId());
    assertEquals(command.planId(), savedRetrospect.getPlanId());
    assertEquals(command.content(), savedRetrospect.getContent());
    assertNotNull(savedRetrospect.getId());
  }
}