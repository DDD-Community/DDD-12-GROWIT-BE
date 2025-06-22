package com.growit.app.user.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RequiredConsentRequest {
  private boolean isPrivacyPolicyAgreed;
  private boolean isServiceTermsAgreed;
}
