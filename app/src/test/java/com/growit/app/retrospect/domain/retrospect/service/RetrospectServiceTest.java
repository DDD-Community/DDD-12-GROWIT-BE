package com.growit.app.retrospect.domain.retrospect.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.retrospect.FakeRetrospectRepository;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RetrospectServiceTest {

  private FakeRetrospectRepository retrospectRepository;

  private RetrospectService retrospectService;

  @BeforeEach
  void setUp() {
    retrospectRepository = new FakeRetrospectRepository();
    retrospectService = new RetrospectService(retrospectRepository);
  }

  @Test
  void givenExistingRetrospect_whenCheckUniqueRetrospect_throwBadRequestException() {
    Retrospect existingRetrospect = RetrospectFixture.defaultRetrospect();
    retrospectRepository.saveRetrospect(existingRetrospect);

    assertThrows(
        BadRequestException.class,
        () -> retrospectService.checkUniqueRetrospect(existingRetrospect.getPlanId()));
  }

  @Test
  void givenNoExistingRetrospect_whenCheckUniqueRetrospect_thenSuccess() {
    Retrospect existingRetrospect = RetrospectFixture.defaultRetrospect();
    retrospectRepository.saveRetrospect(existingRetrospect);

    assertDoesNotThrow(() -> retrospectService.checkUniqueRetrospect("newPlanId"));
  }

  @Test
  void givenMismatchedUserId_whenCheckMyRetrospect_thenThrowsBadRequestException() {
    // Given
    Retrospect retrospect = RetrospectFixture.defaultRetrospect();

    // When & Then
    assertThrows(
        BadRequestException.class,
        () -> retrospectService.checkMyRetrospect(retrospect, "invalidUserId"));
  }

  @Test
  void givenMatchingUserId_whenCheckMyRetrospect_thenDoesNotThrow() {
    // Given
    Retrospect retrospect = RetrospectFixture.defaultRetrospect();

    // When & Then
    assertDoesNotThrow(
        () -> retrospectService.checkMyRetrospect(retrospect, retrospect.getUserId()));
  }
}
