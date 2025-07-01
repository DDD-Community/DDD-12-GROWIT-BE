package com.growit.app.goal.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.growit.app.goal.domain.retrospect.Retrospect;
import com.growit.app.goal.domain.retrospect.RetrospectRepository;
import com.growit.app.goal.domain.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.goal.domain.retrospect.service.RetrospectValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateRetrospectUseCaseTest {

  @Mock
  private RetrospectValidator retrospectValidator;

  @Mock
  private RetrospectRepository retrospectRepository;

  @InjectMocks
  private CreateRetrospectUseCase createRetrospectUseCase;

  private CreateRetrospectCommand command;

  @BeforeEach
  void setUp() {
    command = new CreateRetrospectCommand(
        "goal-id",
        "plan-id",
        "user-id",
        "이번 주 회고입니다. 잘 진행되었습니다.");
  }

  @Test
  void givenValidCommand_whenExecute_thenReturnRetrospectId() {
    // given
    doNothing().when(retrospectValidator).validateCreateRetrospect(command);

    // when
    String retrospectId = createRetrospectUseCase.execute(command);

    // then
    assertNotNull(retrospectId);
    verify(retrospectValidator).validateCreateRetrospect(command);
    verify(retrospectRepository).saveRetrospect(any(Retrospect.class));
  }
}