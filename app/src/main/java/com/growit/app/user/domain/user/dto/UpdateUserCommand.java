package com.growit.app.user.domain.user.dto;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.CareerYear;

public record UpdateUserCommand(
    User user, String name, String lastName, String jobRoleId, CareerYear careerYear) {}
