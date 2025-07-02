package com.growit.app.retrospect.domain.retrospect.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrospectService implements RetrospectValidator {
  private final RetrospectRepository retrospectRepository;

  @Override
  public void checkUniqueRetrospect(String planId) throws BadRequestException {
    retrospectRepository
        .findByPlanId(planId)
        .ifPresent(
            existing -> {
              throw new BadRequestException("해당 주간 계획에 대한 회고가 이미 존재합니다.");
            });
  }

  @Override
  public void checkMyRetrospect(Retrospect retrospect, String userId) throws BadRequestException {
    if (!retrospect.getUserId().equals(userId)) {
      throw new BadRequestException("해당 정보가 올바르지 않습니다.");
    }
  }
}
