package com.growit.app.user.domain.user.dto;

public record SignUpAppleCommand(
    String registrationToken, String name) {}
