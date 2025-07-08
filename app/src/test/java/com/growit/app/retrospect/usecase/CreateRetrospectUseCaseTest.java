package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.domain.retrospect.command.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectValidator;
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

    // When
    String result = createRetrospectUseCase.execute(command);

    // Then
    assertNotNull(result);
  }
}
