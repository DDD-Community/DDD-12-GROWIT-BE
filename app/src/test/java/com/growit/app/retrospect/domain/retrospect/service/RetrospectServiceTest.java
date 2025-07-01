package com.growit.app.retrospect.domain.retrospect.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetrospectServiceTest {

  @Mock private RetrospectRepository retrospectRepository;
  @Mock private GoalRepository goalRepository;

  @InjectMocks private RetrospectService retrospectService;

  @Test
  void givenValidContent_whenValidateContent_thenSuccess() {
    String validContent = "이번 주는 정말 유익한 한 주였습니다.";

    assertDoesNotThrow(() -> retrospectService.validateContent(validContent));
  }

  @Test
  void givenNullContent_whenValidateContent_throwBadRequestException() {
    assertThrows(BadRequestException.class, () -> retrospectService.validateContent(null));
  }

  @Test
  void givenEmptyContent_whenValidateContent_throwBadRequestException() {
    assertThrows(BadRequestException.class, () -> retrospectService.validateContent(""));
  }

  @Test
  void givenShortContent_whenValidateContent_throwBadRequestException() {
    String shortContent = "짧음";
    assertThrows(BadRequestException.class, () -> retrospectService.validateContent(shortContent));
  }

  @Test
  void givenLongContent_whenValidateContent_throwBadRequestException() {
    String longContent = "a".repeat(201);
    assertThrows(BadRequestException.class, () -> retrospectService.validateContent(longContent));
  }

  @Test
  void givenExistingRetrospect_whenValidateUniqueRetrospect_throwBadRequestException() {
    Retrospect existingRetrospect = Retrospect.builder()
        .id("existing-id")
        .goalId("goal-123")
        .planId("plan-456")
        .content("기존 회고")
        .build();
    
    when(retrospectRepository.findByGoalIdAndPlanId("goal-123", "plan-456"))
        .thenReturn(Optional.of(existingRetrospect));

    assertThrows(BadRequestException.class, 
        () -> retrospectService.validateUniqueRetrospect("goal-123", "plan-456"));
  }

  @Test
  void givenNoExistingRetrospect_whenValidateUniqueRetrospect_thenSuccess() {
    when(retrospectRepository.findByGoalIdAndPlanId("goal-123", "plan-456"))
        .thenReturn(Optional.empty());

    assertDoesNotThrow(() -> retrospectService.validateUniqueRetrospect("goal-123", "plan-456"));
  }

  @Test
  void givenNonExistentGoal_whenValidatePlanExists_throwNotFoundException() {
    when(goalRepository.findById("non-existent-goal"))
        .thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, 
        () -> retrospectService.validatePlanExists("non-existent-goal", "plan-456"));
  }

  @Test
  void givenValidGoalAndPlan_whenValidatePlanExists_thenSuccess() {
    Goal goal = GoalFixture.defaultGoal();
    when(goalRepository.findById(anyString()))
        .thenReturn(Optional.of(goal));

    // Get the first plan ID from the goal's plans
    String existingPlanId = goal.getPlans().get(0).getId();

    assertDoesNotThrow(() -> retrospectService.validatePlanExists("goal-123", existingPlanId));
  }

  @Test
  void givenNonExistentPlan_whenValidatePlanExists_throwNotFoundException() {
    Goal goal = GoalFixture.defaultGoal();
    when(goalRepository.findById(anyString()))
        .thenReturn(Optional.of(goal));

    assertThrows(NotFoundException.class, 
        () -> retrospectService.validatePlanExists("goal-123", "non-existent-plan"));
  }
}