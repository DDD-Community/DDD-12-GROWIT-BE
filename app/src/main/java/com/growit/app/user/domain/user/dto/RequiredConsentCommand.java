package com.growit.app.user.domain.user.dto;

import static com.growit.app.common.util.message.ErrorCode.USER_REQUIRED_INVALID;

import com.growit.app.common.exception.BadRequestException;

public record RequiredConsentCommand(boolean isPrivacyPolicyAgreed, boolean isServiceTermsAgreed) {
  public void checkRequiredConsent() {
    if (!isPrivacyPolicyAgreed() || !isServiceTermsAgreed()) {
      throw new BadRequestException(USER_REQUIRED_INVALID.getCode());
    }
  }
}
