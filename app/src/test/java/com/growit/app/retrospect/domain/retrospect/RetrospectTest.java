package com.growit.app.retrospect.domain.retrospect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.retrospect.domain.retrospect.command.CreateRetrospectCommand;
import org.junit.jupiter.api.Test;

class RetrospectTest {

  @Test
  void givenValidCommand_whenCreateFromCommand_thenRetrospectIsCreated() {
    // Given
    CreateRetrospectCommand command = RetrospectFixture.defaultCreateRetrospectCommand();

    // When
    Retrospect retrospect = Retrospect.from(command);

    // Then
    assertNotNull(retrospect.getId());
    assertEquals(command.goalId(), retrospect.getGoalId());
    assertEquals(command.userId(), retrospect.getUserId());
    assertEquals(command.planId(), retrospect.getPlanId());
    assertEquals(command.content(), retrospect.getContent());
  }
}
