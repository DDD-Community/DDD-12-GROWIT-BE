package com.growit.app.user.domain.user.dto;

import com.growit.app.user.domain.user.vo.CareerYear;

public record SignUpKaKaoCommand(
    String name, String jobRoleId, CareerYear careerYear, String registrationToken) {}
