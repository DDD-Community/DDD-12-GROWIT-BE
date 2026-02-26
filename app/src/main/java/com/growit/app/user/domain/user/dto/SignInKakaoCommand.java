package com.growit.app.user.domain.user.dto;

public record SignInKakaoCommand(String idToken, String refreshToken, String nonce) {}
