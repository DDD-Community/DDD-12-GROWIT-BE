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

  @Test
  void givenRetrospectBuilder_whenBuild_thenRetrospectIsCreated() {
    // Given & When
    Retrospect retrospect =
        Retrospect.builder()
            .id("test-id")
            .userId("user-id")
            .goalId("goal-123")
            .planId("plan-456")
            .content("Test retrospect content")
            .build();

    // Then
    assertEquals("test-id", retrospect.getId());
    assertEquals("goal-123", retrospect.getGoalId());
    assertEquals("plan-456", retrospect.getPlanId());
    assertEquals("Test retrospect content", retrospect.getContent());
  }
}
