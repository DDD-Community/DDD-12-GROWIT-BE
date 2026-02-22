package com.growit.app.user.domain.user.dto;

import com.growit.app.user.domain.user.vo.CareerYear;

public record SignUpAppleCommand(
    String registrationToken, String name, String jobRoleId, CareerYear careerYear) {}
