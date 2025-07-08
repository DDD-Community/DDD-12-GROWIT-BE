package com.growit.app.retrospect.domain.retrospect.service;

import static com.growit.app.common.util.message.ErrorCode.RETROSPECT_ALREADY_EXISTS_BY_PLAN;
import static com.growit.app.common.util.message.ErrorCode.RETROSPECT_NOT_FOUND;

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
              throw new BadRequestException(RETROSPECT_ALREADY_EXISTS_BY_PLAN.getCode());
            });
  }

  @Override
  public Retrospect getMyRetrospect(String id, String userId) throws NotFoundException {
    return retrospectRepository
        .findByIdAndUserId(id, userId)
        .orElseThrow(() -> new NotFoundException(RETROSPECT_NOT_FOUND.getCode()));
  }
}
