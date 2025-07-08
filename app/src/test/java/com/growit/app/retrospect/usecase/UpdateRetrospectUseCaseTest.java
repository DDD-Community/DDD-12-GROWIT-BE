package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.domain.retrospect.command.UpdateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateRetrospectUseCaseTest {

  @Mock private RetrospectQuery retrospectQuery;

  @Mock private RetrospectRepository retrospectRepository;

  @InjectMocks private UpdateRetrospectUseCase useCase;

  @Test
  void givenInvalidCommand_whenExecute_thenReturnsNotFoundException() {
    // Given
    String id = "id";
    String userId = "invalidUserId";
    when(retrospectQuery.getMyRetrospect(id, userId)).thenThrow(NotFoundException.class);

    UpdateRetrospectCommand command = RetrospectFixture.updateRetrospectCommand(id, userId, "이번 주");
    // Then
    assertThrows(NotFoundException.class, () -> useCase.execute(command));
  }
}
