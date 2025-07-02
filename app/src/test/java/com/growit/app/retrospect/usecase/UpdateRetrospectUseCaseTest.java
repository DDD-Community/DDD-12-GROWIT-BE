package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.retrospect.FakeRetrospectRepository;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.command.UpdateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectService;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateRetrospectUseCaseTest {

  private UpdateRetrospectUseCase updateRetrospectUseCase;
  private FakeRetrospectRepository retrospectRepository;

  @BeforeEach
  void setUp() {
    retrospectRepository = new FakeRetrospectRepository();
    RetrospectValidator retrospectValidator = new RetrospectService(retrospectRepository);
    updateRetrospectUseCase =
        new UpdateRetrospectUseCase(retrospectValidator, retrospectRepository);
  }

  @Test
  void givenNotExistRetrospect_whenExecute_thenReturnsNotFoundException() {
    // Given
    UpdateRetrospectCommand command =
        RetrospectFixture.updateRetrospectCommand("id", "userId", "이번 주");
    // Then
    assertThrows(NotFoundException.class, () -> updateRetrospectUseCase.execute(command));
  }

  @Test
  void givenInvalidCommand_whenExecute_thenReturnsBadRequestException() {
    // Given
    Retrospect retrospect = RetrospectFixture.defaultRetrospect();
    retrospectRepository.saveRetrospect(retrospect);

    UpdateRetrospectCommand command =
        RetrospectFixture.updateRetrospectCommand(retrospect.getId(), "invalidUserId", "이번 주");
    // Then
    assertThrows(BadRequestException.class, () -> updateRetrospectUseCase.execute(command));
  }
}
