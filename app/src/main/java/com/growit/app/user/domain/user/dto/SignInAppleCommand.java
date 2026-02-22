package com.growit.app.user.domain.user.dto;

import lombok.Builder;

@Builder
public record SignInAppleCommand(String idToken, String authorizationCode) {}
