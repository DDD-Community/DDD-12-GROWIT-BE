package com.growit.app.goal.domain.retrospect.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.retrospect.RetrospectRepository;
import com.growit.app.goal.domain.retrospect.dto.CreateRetrospectCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrospectService implements RetrospectValidator {
  private final RetrospectRepository retrospectRepository;
  private final GoalRepository goalRepository;

  @Override
  public void validateCreateRetrospect(CreateRetrospectCommand command) throws BadRequestException {
    // Validate content length
    if (command.content() == null
        || command.content().length() < 10
        || command.content().length() > 200) {
      throw new BadRequestException("회고 내용은 10자 이상 200자 이하여야 합니다.");
    }

    // Check if goal exists and contains the plan
    var goal =
        goalRepository
            .findById(command.goalId())
            .orElseThrow(() -> new NotFoundException("목표를 찾을 수 없습니다."));

    // Check if plan exists in the goal
    boolean planExists =
        goal.getPlans().stream().anyMatch(plan -> plan.getId().equals(command.planId()));

    if (!planExists) {
      throw new NotFoundException("해당 목표에서 계획을 찾을 수 없습니다.");
    }

    // Check if retrospect already exists for this goal and plan
    if (retrospectRepository
        .findByGoalIdAndPlanId(command.goalId(), command.planId())
        .isPresent()) {
      throw new BadRequestException("해당 계획에 대한 회고가 이미 존재합니다.");
    }
  }
}
