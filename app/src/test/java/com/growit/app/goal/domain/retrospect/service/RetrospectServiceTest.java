package com.growit.app.goal.domain.retrospect.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.retrospect.Retrospect;
import com.growit.app.goal.domain.retrospect.RetrospectRepository;
import com.growit.app.goal.domain.retrospect.dto.CreateRetrospectCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RetrospectServiceTest {

  @Mock
  private RetrospectRepository retrospectRepository;

  @Mock
  private GoalRepository goalRepository;

  @InjectMocks
  private RetrospectService retrospectService;

  private CreateRetrospectCommand validCommand;
  private Goal goal;
  private Plan plan;

  @BeforeEach
  void setUp() {
    plan = Plan.builder()
        .id("plan-id")
        .weekOfMonth(1)
        .content("주간 계획")
        .build();

    goal = Goal.builder()
        .id("goal-id")
        .userId("user-id")
        .name("목표")
        .plans(List.of(plan))
        .build();

    validCommand = new CreateRetrospectCommand(
        "goal-id",
        "plan-id",
        "user-id",
        "이번 주 회고입니다. 잘 진행되었습니다.");
  }

  @Test
  void givenValidCommand_whenValidateCreateRetrospect_thenSuccess() {
    // given
    when(goalRepository.findById("goal-id")).thenReturn(Optional.of(goal));
    when(retrospectRepository.findByGoalIdAndPlanId("goal-id", "plan-id")).thenReturn(Optional.empty());

    // when & then
    retrospectService.validateCreateRetrospect(validCommand);
  }

  @Test
  void givenShortContent_whenValidateCreateRetrospect_thenThrowBadRequestException() {
    // given
    CreateRetrospectCommand shortContentCommand = new CreateRetrospectCommand(
        "goal-id", "plan-id", "user-id", "짧은 내용");

    // when & then
    assertThrows(BadRequestException.class, 
        () -> retrospectService.validateCreateRetrospect(shortContentCommand));
  }

  @Test
  void givenLongContent_whenValidateCreateRetrospect_thenThrowBadRequestException() {
    // given
    String longContent = "a".repeat(201);
    CreateRetrospectCommand longContentCommand = new CreateRetrospectCommand(
        "goal-id", "plan-id", "user-id", longContent);

    // when & then
    assertThrows(BadRequestException.class, 
        () -> retrospectService.validateCreateRetrospect(longContentCommand));
  }

  @Test
  void givenNonExistentGoal_whenValidateCreateRetrospect_thenThrowNotFoundException() {
    // given
    when(goalRepository.findById("goal-id")).thenReturn(Optional.empty());

    // when & then
    assertThrows(NotFoundException.class, 
        () -> retrospectService.validateCreateRetrospect(validCommand));
  }

  @Test
  void givenNonExistentPlan_whenValidateCreateRetrospect_thenThrowNotFoundException() {
    // given
    Goal goalWithoutPlan = Goal.builder()
        .id("goal-id")
        .userId("user-id")
        .name("목표")
        .plans(List.of())
        .build();
    
    when(goalRepository.findById("goal-id")).thenReturn(Optional.of(goalWithoutPlan));

    // when & then
    assertThrows(NotFoundException.class, 
        () -> retrospectService.validateCreateRetrospect(validCommand));
  }

  @Test
  void givenExistingRetrospect_whenValidateCreateRetrospect_thenThrowBadRequestException() {
    // given
    Retrospect existingRetrospect = Retrospect.builder()
        .id("existing-id")
        .goalId("goal-id")
        .planId("plan-id")
        .content("기존 회고")
        .build();

    when(goalRepository.findById("goal-id")).thenReturn(Optional.of(goal));
    when(retrospectRepository.findByGoalIdAndPlanId("goal-id", "plan-id"))
        .thenReturn(Optional.of(existingRetrospect));

    // when & then
    assertThrows(BadRequestException.class, 
        () -> retrospectService.validateCreateRetrospect(validCommand));
  }
}