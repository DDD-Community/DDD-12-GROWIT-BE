package com.growit.app.user.usecase;

import com.growit.app.user.controller.dto.response.OAuthResponse;
import com.growit.app.user.controller.dto.response.TokenResponse;

public record SignInAppleResult(
    boolean isPendingSignup, TokenResponse tokenResponse, OAuthResponse oauthResponse) {}
