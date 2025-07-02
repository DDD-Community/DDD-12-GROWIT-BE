package com.growit.app.retrospect.domain.retrospect.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetrospectService implements RetrospectValidator {
  private final RetrospectRepository retrospectRepository;

  @Override
  public void checkContent(String content) throws BadRequestException {
    if (content == null || content.trim().isEmpty()) {
      throw new BadRequestException("회고 내용을 입력해주세요.");
    }

    String trimmedContent = content.trim();
    if (trimmedContent.length() < 10) {
      throw new BadRequestException("회고 내용은 최소 10자 이상 입력해주세요.");
    }

    if (trimmedContent.length() > 200) {
      throw new BadRequestException("회고 내용은 최대 200자까지 입력 가능합니다.");
    }
  }

  @Override
  public void checkUniqueRetrospect(String planId) throws BadRequestException {
    retrospectRepository
        .findByPlanId(planId)
        .ifPresent(
            existing -> {
              throw new BadRequestException("해당 주간 계획에 대한 회고가 이미 존재합니다.");
            });
  }
}
