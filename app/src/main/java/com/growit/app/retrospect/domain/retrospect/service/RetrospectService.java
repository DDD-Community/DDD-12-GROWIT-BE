package com.growit.app.retrospect.domain.retrospect.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.exception.NotFoundException;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrospectService implements RetrospectValidator, RetrospectQuery {
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
  public Retrospect getMyRetrospect(String id, String userId) throws NotFoundException {
    return retrospectRepository
        .findByIdAndUserId(id, userId)
        .orElseThrow(() -> new NotFoundException("회고 정보가 존재하지 않습니다. "));
  }
}
