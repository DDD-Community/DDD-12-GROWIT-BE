package com.growit.app.user.domain.user.dto;

public record RequiredConsentCommand(boolean isPrivacyPolicyAgreed, boolean isServiceTermsAgreed) {}
