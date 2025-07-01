package com.growit.app.goal.domain.goal.retrospect.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.retrospect.Retrospect;
import com.growit.app.goal.domain.goal.retrospect.RetrospectRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RetrospectServiceTest {

  @Mock private RetrospectRepository retrospectRepository;
  @Mock private GoalRepository goalRepository;

  private RetrospectService retrospectService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    retrospectService = new RetrospectService(retrospectRepository, goalRepository);
  }

  @Test
  void givenValidContent_whenValidateContent_thenSuccess() {
    String validContent = "이번 주 계획을 잘 수행했습니다.";
    
    assertDoesNotThrow(() -> retrospectService.validateContent(validContent));
  }

  @Test
  void givenNullContent_whenValidateContent_thenThrowBadRequestException() {
    assertThrows(BadRequestException.class, () -> retrospectService.validateContent(null));
  }

  @Test
  void givenEmptyContent_whenValidateContent_thenThrowBadRequestException() {
    assertThrows(BadRequestException.class, () -> retrospectService.validateContent(""));
  }

  @Test
  void givenTooShortContent_whenValidateContent_thenThrowBadRequestException() {
    String shortContent = "짧은 내용";
    assertThrows(BadRequestException.class, () -> retrospectService.validateContent(shortContent));
  }

  @Test
  void givenTooLongContent_whenValidateContent_thenThrowBadRequestException() {
    String longContent = "a".repeat(201);
    assertThrows(BadRequestException.class, () -> retrospectService.validateContent(longContent));
  }

  @Test
  void givenValidGoalAndPlan_whenValidatePlanExists_thenSuccess() {
    String goalId = "goal-123";
    String planId = "plan-456";
    
    Plan plan = Plan.builder()
        .id(planId)
        .weekOfMonth(1)
        .content("주간 계획")
        .build();
    
    Goal goal = Goal.builder()
        .id(goalId)
        .plans(List.of(plan))
        .build();
    
    when(goalRepository.findById(goalId)).thenReturn(Optional.of(goal));
    
    assertDoesNotThrow(() -> retrospectService.validatePlanExists(goalId, planId));
  }

  @Test
  void givenNonExistentGoal_whenValidatePlanExists_thenThrowNotFoundException() {
    String goalId = "nonexistent-goal";
    String planId = "plan-456";
    
    when(goalRepository.findById(goalId)).thenReturn(Optional.empty());
    
    assertThrows(NotFoundException.class, () -> retrospectService.validatePlanExists(goalId, planId));
  }

  @Test
  void givenNonExistentPlan_whenValidatePlanExists_thenThrowNotFoundException() {
    String goalId = "goal-123";
    String planId = "nonexistent-plan";
    
    Plan plan = Plan.builder()
        .id("different-plan")
        .weekOfMonth(1)
        .content("주간 계획")
        .build();
    
    Goal goal = Goal.builder()
        .id(goalId)
        .plans(List.of(plan))
        .build();
    
    when(goalRepository.findById(goalId)).thenReturn(Optional.of(goal));
    
    assertThrows(NotFoundException.class, () -> retrospectService.validatePlanExists(goalId, planId));
  }

  @Test
  void givenNewRetrospect_whenValidateUniqueRetrospect_thenSuccess() {
    String goalId = "goal-123";
    String planId = "plan-456";
    
    when(retrospectRepository.findByGoalIdAndPlanId(goalId, planId)).thenReturn(Optional.empty());
    
    assertDoesNotThrow(() -> retrospectService.validateUniqueRetrospect(goalId, planId));
  }

  @Test
  void givenExistingRetrospect_whenValidateUniqueRetrospect_thenThrowBadRequestException() {
    String goalId = "goal-123";
    String planId = "plan-456";
    
    Retrospect existingRetrospect = Retrospect.builder()
        .id("retrospect-789")
        .goalId(goalId)
        .planId(planId)
        .content("기존 회고")
        .build();
    
    when(retrospectRepository.findByGoalIdAndPlanId(goalId, planId))
        .thenReturn(Optional.of(existingRetrospect));
    
    assertThrows(BadRequestException.class, () -> retrospectService.validateUniqueRetrospect(goalId, planId));
  }
}