package com.growit.app.user.domain.user.dto;

import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.user.domain.user.User;

public record UserDto(User user, JobRole jobRole) {}
