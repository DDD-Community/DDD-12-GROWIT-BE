package com.growit.user.domain.user.dto;

public record RequiredConsentCommand(boolean isPrivacyPolicyAgreed, boolean isServiceTermsAgreed) {}
