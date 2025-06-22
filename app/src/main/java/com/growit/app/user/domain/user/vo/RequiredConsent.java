package com.growit.app.user.domain.user.vo;


public record RequiredConsent(
  boolean isPrivacyPolicyAgreed,
  boolean isServiceTermsAgreed
) {}
