package com.growit.app.retrospect.domain.retrospect.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.retrospect.FakeRetrospectRepository;
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
  void givenValidContent_whenCheckContent_thenSuccess() {
    String validContent = "이번 주는 정말 유익한 한 주였습니다.";

    assertDoesNotThrow(() -> retrospectService.checkContent(validContent));
  }

  @Test
  void givenNullContent_whenCheckContent_throwBadRequestException() {
    assertThrows(BadRequestException.class, () -> retrospectService.checkContent(null));
  }

  @Test
  void givenEmptyContent_whenCheckContent_throwBadRequestException() {
    assertThrows(BadRequestException.class, () -> retrospectService.checkContent(""));
  }

  @Test
  void givenShortContent_whenCheckContent_throwBadRequestException() {
    String shortContent = "짧음";
    assertThrows(BadRequestException.class, () -> retrospectService.checkContent(shortContent));
  }

  @Test
  void givenLongContent_whenCheckContent_throwBadRequestException() {
    String longContent = "a".repeat(201);
    assertThrows(BadRequestException.class, () -> retrospectService.checkContent(longContent));
  }

  @Test
  void givenExistingRetrospect_whenCheckUniqueRetrospect_throwBadRequestException() {
    Retrospect existingRetrospect =
        Retrospect.builder()
            .id("existing-id")
            .goalId("goal-123")
            .planId("plan-456")
            .content("기존 회고")
            .build();
    retrospectRepository.saveRetrospect(existingRetrospect);

    assertThrows(
        BadRequestException.class, () -> retrospectService.checkUniqueRetrospect("plan-456"));
  }

  @Test
  void givenNoExistingRetrospect_whenCheckUniqueRetrospect_thenSuccess() {
    Retrospect existingRetrospect =
        Retrospect.builder()
            .id("existing-id")
            .goalId("goal-123")
            .planId("plan-456")
            .content("기존 회고")
            .build();
    retrospectRepository.saveRetrospect(existingRetrospect);

    assertDoesNotThrow(() -> retrospectService.checkUniqueRetrospect("planId"));
  }
}
