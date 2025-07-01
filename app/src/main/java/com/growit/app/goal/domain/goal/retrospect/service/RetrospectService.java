package com.growit.app.goal.domain.goal.retrospect.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.retrospect.RetrospectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrospectService implements RetrospectValidator {
  
  private final RetrospectRepository retrospectRepository;
  private final GoalRepository goalRepository;

  @Override
  public void validateContent(String content) throws BadRequestException {
    if (content == null || content.trim().isEmpty()) {
      throw new BadRequestException("회고 내용은 필수입니다.");
    }
    
    String trimmedContent = content.trim();
    if (trimmedContent.length() < 10) {
      throw new BadRequestException("회고 내용은 최소 10자 이상이어야 합니다.");
    }
    
    if (trimmedContent.length() > 200) {
      throw new BadRequestException("회고 내용은 최대 200자까지 입력 가능합니다.");
    }
  }

  @Override
  public void validatePlanExists(String goalId, String planId) throws NotFoundException {
    Goal goal = goalRepository.findById(goalId)
        .orElseThrow(() -> new NotFoundException("목표를 찾을 수 없습니다."));
    
    boolean planExists = goal.getPlans().stream()
        .anyMatch(plan -> plan.getId().equals(planId));
    
    if (!planExists) {
      throw new NotFoundException("해당 목표에서 계획을 찾을 수 없습니다.");
    }
  }

  @Override
  public void validateUniqueRetrospect(String goalId, String planId) throws BadRequestException {
    if (retrospectRepository.findByGoalIdAndPlanId(goalId, planId).isPresent()) {
      throw new BadRequestException("해당 주차에 대한 회고가 이미 존재합니다.");
    }
  }
}